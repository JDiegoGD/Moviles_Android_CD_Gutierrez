package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.entity.Servicio
import com.gutierrez.huellitasvet.data.prefs.PreferenciasStore
import com.gutierrez.huellitasvet.data.local.entity.HorarioAtencion
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

/** Estado y reglas de la pantalla Pedir cita. */
class PedirCitaViewModel(
    private val repo: VetRepository,
    prefs: PreferenciasStore,
    duenoId: Int,
    mascotaInicial: Int? = null
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Solo servicios ACTIVOS: los inactivos no aparecen al pedir cita
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val aceptaCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _mascotaId = MutableStateFlow(mascotaInicial)
    val mascotaId: StateFlow<Int?> = _mascotaId.asStateFlow()

    private val _servicioId = MutableStateFlow<Int?>(null)
    val servicioId: StateFlow<Int?> = _servicioId.asStateFlow()

    private val _fecha = MutableStateFlow(LocalDate.now()) // por defecto: hoy
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** De hoy a 7 dias. */
    val dias: List<LocalDate> = (0L..7L).map { LocalDate.now().plusDays(it) }

    /** Horas ocupadas: cambia sola cuando cambia la fecha (flatMapLatest). */
    @OptIn(ExperimentalCoroutinesApi::class)
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** El boton "Confirmar cita" se habilita solo cuando todo esta elegido. */
    /** Horario de atencion que configuro el veterinario. */
    val horario: StateFlow<List<HorarioAtencion>> = repo.observarHorario()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Horas que se pueden reservar en la fecha elegida (segun el horario de atencion). */
    val horasDelDia: StateFlow<List<String>> = combine(_fecha, horario) { f, lista ->
        Horarios.horasDe(Horarios.delDia(lista, f))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val puedeConfirmar: StateFlow<Boolean> = combine(_mascotaId, _servicioId, _hora, aceptaCitas) { m, s, h, ok ->
        m != null && s != null && h != null && ok
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun elegirMascota(id: Int) { _mascotaId.value = id }

    fun elegirServicio(id: Int) { _servicioId.value = id }

    /** Si la fecha cambia, se limpia la hora elegida. */
    fun elegirFecha(f: LocalDate) {
        _fecha.value = f
        _hora.value = null
    }

    fun elegirHora(h: String) { _hora.value = h }

    /** Si la fecha es hoy, las horas que ya pasaron se deshabilitan. */
    fun horaPasada(fecha: LocalDate, hora: String): Boolean =
        fecha == LocalDate.now() && !LocalTime.parse(hora).isAfter(LocalTime.now())

    fun limpiarMensaje() { _mensaje.value = null }

    fun confirmar(onCreada: () -> Unit) {
        val mascotaId = _mascotaId.value ?: return
        val servicioId = _servicioId.value ?: return
        val hora = _hora.value ?: return
        val fecha = _fecha.value
        viewModelScope.launch {
            if (!aceptaCitas.value) {
                _mensaje.value = "La clínica no está recibiendo citas"
                return@launch
            }
            if (horaPasada(fecha, hora)) {
                _mensaje.value = "Esa hora ya pasó. Elige otra."
                _hora.value = null
                return@launch
            }
            if (hora !in horasDelDia.value) {
                _mensaje.value = "Esa hora está fuera del horario de atención."
                _hora.value = null
                return@launch
            }
            // Antes de crear, vuelve a consultar si el horario sigue libre
            val ocupadas = repo.observarHorasOcupadas(fecha.toString()).first()
            if (hora in ocupadas) {
                _mensaje.value = "Ese horario se acaba de ocupar. Elige otro."
                _hora.value = null
                return@launch
            }
            val id = repo.crearCita(mascotaId, servicioId, fecha.toString(), hora, "")
            if (id > 0) {
                onCreada()
            } else {
                _mensaje.value = "No se pudo crear la cita. Intenta con otro horario."
                _hora.value = null
            }
        }
    }
}
