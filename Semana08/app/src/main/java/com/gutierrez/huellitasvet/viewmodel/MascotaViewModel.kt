package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.entity.Vacuna
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Registrar/editar/eliminar mascota y consultar su historial clinico. */
class MascotaViewModel(private val repo: VetRepository) : ViewModel() {

    // Errores por campo: "nombre", "especie", "raza", "sexo", "anio", "peso", "fecha", "proximaDosis"
    private val _errores = MutableStateFlow<Map<String, String>>(emptyMap())
    val errores: StateFlow<Map<String, String>> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val historiales = mutableMapOf<Int, StateFlow<List<CitaConDetalle>>>()
    private val vacunasCache = mutableMapOf<Int, StateFlow<List<Vacuna>>>()

    fun mascota(id: Int): Flow<Mascota?> = repo.observarMascota(id)

    /** Historial clinico: citas ATENDIDAS, la mas reciente primero. */
    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> =
        historiales.getOrPut(mascotaId) {
            repo.observarHistorial(mascotaId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        }

    fun vacunas(mascotaId: Int): StateFlow<List<Vacuna>> =
        vacunasCache.getOrPut(mascotaId) {
            repo.observarVacunas(mascotaId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        }

    /** Reglas: nombre >= 2, especie y raza elegidas, sexo elegido, año entre 1990 y el actual, peso > 0. */
    private fun validar(m: Mascota): Map<String, String> {
        val errores = mutableMapOf<String, String>()
        val anioActual = LocalDate.now().year
        if (m.nombre.trim().length < 2) errores["nombre"] = "Mínimo 2 caracteres"
        if (m.especie.isBlank()) errores["especie"] = "Elige una especie"
        if (m.raza.isBlank()) errores["raza"] = "Selecciona una raza"
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

    fun guardarVacuna(mascotaId: Int, nombre: String, fecha: String, proximaDosis: String?, onListo: () -> Unit) {
        val errores = mutableMapOf<String, String>()
        val hoy = LocalDate.now()

        if (nombre.trim().length < 3) {
            errores["nombre"] = "Mínimo 3 caracteres"
        }

        val fechaParsed = try {
            LocalDate.parse(fecha.trim())
        } catch (e: Exception) {
            null
        }

        if (fechaParsed == null) {
            errores["fecha"] = "Fecha inválida (aaaa-MM-dd)"
        } else if (fechaParsed.isAfter(hoy)) {
            errores["fecha"] = "No puede ser una fecha futura"
        }

        if (!proximaDosis.isNullOrBlank()) {
            val proxParsed = try {
                LocalDate.parse(proximaDosis.trim())
            } catch (e: Exception) {
                null
            }
            if (proxParsed == null) {
                errores["proximaDosis"] = "Fecha inválida (aaaa-MM-dd)"
            } else if (fechaParsed != null && !proxParsed.isAfter(fechaParsed)) {
                errores["proximaDosis"] = "Debe ser posterior a la fecha"
            }
        }

        _errores.value = errores
        if (errores.isNotEmpty()) return

        viewModelScope.launch {
            repo.guardarVacuna(
                Vacuna(
                    mascotaId = mascotaId,
                    nombre = nombre.trim(),
                    fecha = fecha.trim(),
                    proximaDosis = proximaDosis?.takeIf { it.isNotBlank() }?.trim()
                )
            )
            onListo()
        }
    }

    fun eliminarVacuna(v: Vacuna) {
        viewModelScope.launch {
            repo.eliminarVacuna(v)
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

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
