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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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

    val dias: List<LocalDate> = (0L..7L).map { LocalDate.now().plusDays(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val puedeConfirmar: StateFlow<Boolean> = combine(_hora, mensaje) { h, _ ->
        h != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun elegirFecha(f: LocalDate) {
        _fecha.value = f
        _hora.value = null
    }

    fun elegirHora(h: String) { _hora.value = h }

    fun horaPasada(fecha: LocalDate, hora: String): Boolean =
        fecha == LocalDate.now() && !LocalTime.parse(hora).isAfter(LocalTime.now())

    fun puedeReprogramarActual(citaConDetalle: CitaConDetalle?): Boolean {
        if (citaConDetalle == null) return false
        val c = citaConDetalle.cita
        val estadoValido = c.estado == EstadoCita.PENDIENTE.name || c.estado == EstadoCita.CONFIRMADA.name
        val faltan = Duration.between(LocalDateTime.now(), Formato.fechaHoraDe(c.fecha, c.hora))
        return estadoValido && faltan > Duration.ofHours(2)
    }

    fun limpiarMensaje() { _mensaje.value = null }

    fun confirmar(onListo: () -> Unit) {
        val h = _hora.value ?: return
        val f = _fecha.value
        viewModelScope.launch {
            val actual = repo.observarCita(citaId).first()
            if (!puedeReprogramarActual(actual)) {
                _mensaje.value = "Ya no puedes reprogramar esta cita"
                return@launch
            }
            if (horaPasada(f, h)) {
                _mensaje.value = "Esa hora ya pasó. Elige otra."
                _hora.value = null
                return@launch
            }
            val ocupadas = repo.observarHorasOcupadas(f.toString()).first()
            if (h in ocupadas && actual?.cita?.let { it.fecha != f.toString() || it.hora != h } == true) {
                _mensaje.value = "Ese horario se acaba de ocupar. Elige otro."
                _hora.value = null
                return@launch
            }
            val exito = repo.reprogramarCita(citaId, f.toString(), h)
            if (exito) {
                onListo()
            } else {
                _mensaje.value = "No se pudo reprogramar la cita. Elige otro horario."
                _hora.value = null
            }
        }
    }
}
