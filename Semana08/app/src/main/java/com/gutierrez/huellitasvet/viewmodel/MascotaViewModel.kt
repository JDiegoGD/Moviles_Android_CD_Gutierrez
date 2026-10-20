package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.entity.Vacuna
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.Formato
import com.gutierrez.huellitasvet.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Registrar/editar/eliminar mascota y consultar su historial clinico. */
class MascotaViewModel(private val repo: VetRepository) : ViewModel() {

    // Errores por campo: "nombre", "especie", "sexo", "anio", "peso"
    private val _errores = MutableStateFlow<Map<String, String>>(emptyMap())
    val errores: StateFlow<Map<String, String>> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val historiales = mutableMapOf<Int, StateFlow<List<CitaConDetalle>>>()
    private val mapaPesos = mutableMapOf<Int, StateFlow<List<DatoGrafico>>>()
    private val mapaVacunas = mutableMapOf<Int, StateFlow<List<Vacuna>>>()

    private val _erroresVacuna = MutableStateFlow<Map<String, String>>(emptyMap())
    val erroresVacuna: StateFlow<Map<String, String>> = _erroresVacuna.asStateFlow()

    fun mascota(id: Int): Flow<Mascota?> = repo.observarMascota(id)

    /** Historial clinico: citas ATENDIDAS, la mas reciente primero. */
    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> =
        historiales.getOrPut(mascotaId) {
            repo.observarHistorial(mascotaId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        }

    /** Puntos de peso para gráfico: citas ATENDIDAS con peso. */
    fun pesos(mascotaId: Int): StateFlow<List<DatoGrafico>> =
        mapaPesos.getOrPut(mascotaId) {
            repo.observarPesos(mascotaId)
                .map { lista ->
                    lista.map { p ->
                        DatoGrafico(
                            etiqueta = Formato.fechaDiaMes(p.fecha),
                            valor = p.peso.toFloat()
                        )
                    }
                }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        }

    /** Reglas: nombre >= 2, especie y sexo elegidos, año entre 1990 y el actual, peso > 0. */
    private fun validar(m: Mascota): Map<String, String> {
        val errores = mutableMapOf<String, String>()
        val anioActual = LocalDate.now().year
        if (m.nombre.trim().length < 2) errores["nombre"] = "Mínimo 2 caracteres"
        if (m.especie.isBlank()) errores["especie"] = "Elige una especie"
        if (m.sexo.isBlank()) errores["sexo"] = "Elige el sexo"
        if (m.anioNacimiento !in 1990..anioActual) errores["anio"] = "Entre 1990 y $anioActual"
        if (m.peso <= 0.0) errores["peso"] = "Debe ser mayor que 0"
        return errores
    }

    fun guardar(m: Mascota, onGuardada: () -> Unit) {
        val errores = validar(m)
        _errores.value = errores
        if (errores.isNotEmpty()) return
        viewModelScope.launch {
            repo.guardarMascota(m.copy(nombre = m.nombre.trim(), raza = m.raza.trim()))
            onGuardada()
        }
    }

    /** Regla: si la mascota tiene citas no se borra y se avisa. */
    fun eliminar(m: Mascota, onEliminada: () -> Unit) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(m.id)) {
                _mensaje.value = "No se puede eliminar a ${m.nombre}: tiene citas registradas"
            } else {
                repo.eliminarMascota(m)
                onEliminada()
            }
        }
    }

    fun limpiarErrores() {
        _errores.value = emptyMap()
    }

    fun limpiarErroresVacuna() {
        _erroresVacuna.value = emptyMap()
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    /** Vacunas de la mascota cacheada por ID. */
    fun vacunas(mascotaId: Int): StateFlow<List<Vacuna>> =
        mapaVacunas.getOrPut(mascotaId) {
            repo.observarVacunas(mascotaId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        }

    /** Validaciones de vacuna: nombre >= 3 caracteres, fecha no futura, próxima dosis posterior a la fecha. */
    fun guardarVacuna(v: Vacuna, onGuardada: () -> Unit) {
        val errores = mutableMapOf<String, String>()
        if (v.nombre.trim().length < 3) errores["nombre"] = "Mínimo 3 caracteres"

        val fechaParsed = runCatching { LocalDate.parse(v.fecha.trim()) }.getOrNull()
        if (fechaParsed == null) {
            errores["fecha"] = "Fecha inválida (aaaa-MM-dd)"
        } else if (fechaParsed.isAfter(LocalDate.now())) {
            errores["fecha"] = "La fecha no puede ser futura"
        }

        if (!v.proximaDosis.isNullOrBlank()) {
            val proxParsed = runCatching { LocalDate.parse(v.proximaDosis.trim()) }.getOrNull()
            if (proxParsed == null) {
                errores["proximaDosis"] = "Fecha inválida (aaaa-MM-dd)"
            } else if (fechaParsed != null && !proxParsed.isAfter(fechaParsed)) {
                errores["proximaDosis"] = "Debe ser posterior a la fecha de vacunación"
            }
        }

        _erroresVacuna.value = errores
        if (errores.isNotEmpty()) return

        viewModelScope.launch {
            repo.guardarVacuna(v.copy(nombre = v.nombre.trim(), fecha = v.fecha.trim(), proximaDosis = v.proximaDosis?.trim()))
            onGuardada()
        }
    }

    fun eliminarVacuna(v: Vacuna) {
        viewModelScope.launch {
            repo.eliminarVacuna(v)
        }
    }
}
