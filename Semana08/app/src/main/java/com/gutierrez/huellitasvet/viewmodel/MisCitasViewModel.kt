package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Cita
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.Formato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

/** Citas del dueño: Proximas / Historial y cancelacion con la regla de 2 horas. */
class MisCitasViewModel(
    private val repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    private fun esProxima(c: CitaConDetalle, ahora: LocalDateTime): Boolean =
        (c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name) &&
            !Formato.fechaHoraDe(c.cita.fecha, c.cita.hora).isBefore(ahora)

    private val todas = repo.observarCitasDeDueno(duenoId)

    /** Proximas = PENDIENTE y CONFIRMADA desde ahora (la mas cercana primero). */
    val proximas: StateFlow<List<CitaConDetalle>> = todas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { esProxima(it, ahora) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Historial = ATENDIDA y CANCELADA y las ya pasadas (la mas reciente primero). */
    val historial: StateFlow<List<CitaConDetalle>> = todas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { !esProxima(it, ahora) }.reversed()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** Regla: solo PENDIENTE o CONFIRMADA y solo si faltan MAS de 2 horas. */
    fun puedeCancelar(cita: Cita): Boolean {
        val estadoValido = cita.estado == EstadoCita.PENDIENTE.name ||
            cita.estado == EstadoCita.CONFIRMADA.name
        val faltan = Duration.between(LocalDateTime.now(), Formato.fechaHoraDe(cita.fecha, cita.hora))
        return estadoValido && faltan > Duration.ofHours(2)
    }

    /** Regla: solo PENDIENTE o CONFIRMADA y solo si faltan MAS de 2 horas. */
    fun puedeReprogramar(cita: Cita): Boolean = puedeCancelar(cita)

    fun cancelar(cita: Cita) {
        if (!puedeCancelar(cita)) {
            _mensaje.value = "Ya no puedes cancelar esta cita"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(cita.id, EstadoCita.CANCELADA)
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}
