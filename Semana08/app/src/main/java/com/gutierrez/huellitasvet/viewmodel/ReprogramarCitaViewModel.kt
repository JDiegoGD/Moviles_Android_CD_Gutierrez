package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.Formato
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/** ViewModel para reprogramar fecha y hora de una cita existente. */
class ReprogramarCitaViewModel(
    private val repo: VetRepository,
    private val citaId: Int
) : ViewModel() {

    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** De hoy a 7 días. */
    val dias: List<LocalDate> = (0L..7L).map { LocalDate.now().plusDays(it) }

    /** Horas ocupadas: cambia sola cuando cambia la fecha (flatMapLatest). */
    @OptIn(ExperimentalCoroutinesApi::class)
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** El botón "Confirmar reprogramación" se habilita cuando se elige una hora. */
    val puedeConfirmar: StateFlow<Boolean> = combine(_hora, cita) { h, c ->
        h != null && c != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        // Carga inicial: preselecciona la fecha y hora actual de la cita
        viewModelScope.launch {
            cita.filterNotNull().first().let { c ->
                _fecha.value = LocalDate.parse(c.cita.fecha)
                _hora.value = c.cita.hora
            }
        }
    }

    /** Regla de negocio: solo PENDIENTE o CONFIRMADA y solo si faltan MÁS de 2 horas. */
    fun puedeReprogramar(c: CitaConDetalle?): Boolean {
        if (c == null) return false
        val estadoValido = c.cita.estado == EstadoCita.PENDIENTE.name ||
            c.cita.estado == EstadoCita.CONFIRMADA.name
        val faltan = Duration.between(LocalDateTime.now(), Formato.fechaHoraDe(c.cita.fecha, c.cita.hora))
        return estadoValido && faltan > Duration.ofHours(2)
    }

    fun elegirFecha(f: LocalDate) {
        _fecha.value = f
        _hora.value = null
    }

    fun elegirHora(h: String) { _hora.value = h }

    /** Si la fecha es hoy, las horas que ya pasaron se deshabilitan. */
    fun horaPasada(fecha: LocalDate, hora: String): Boolean =
        fecha == LocalDate.now() && !LocalTime.parse(hora).isAfter(LocalTime.now())

    fun limpiarMensaje() { _mensaje.value = null }

    fun confirmar(onListo: () -> Unit) {
        val c = cita.value ?: return
        val h = _hora.value ?: return
        val f = _fecha.value

        if (!puedeReprogramar(c)) {
            _mensaje.value = "Ya no puedes reprogramar esta cita"
            return
        }
        if (horaPasada(f, h)) {
            _mensaje.value = "Esa hora ya pasó. Elige otra."
            _hora.value = null
            return
        }

        viewModelScope.launch {
            // Si elige exactamente la misma fecha y hora que ya tiene
            if (f.toString() == c.cita.fecha && h == c.cita.hora) {
                _mensaje.value = "Selecciona un horario diferente para reprogramar"
                return@launch
            }

            // Verificar si el horario elegido está ocupado por otra cita
            val ocupadas = repo.observarHorasOcupadas(f.toString()).first()
            if (h in ocupadas) {
                _mensaje.value = "Ese horario ya está ocupado. Elige otro."
                _hora.value = null
                return@launch
            }

            val exito = repo.reprogramarCita(c.cita.id, f.toString(), h)
            if (exito) {
                onListo()
            } else {
                _mensaje.value = "No se pudo reprogramar la cita. Intenta con otro horario."
                _hora.value = null
            }
        }
    }
}
