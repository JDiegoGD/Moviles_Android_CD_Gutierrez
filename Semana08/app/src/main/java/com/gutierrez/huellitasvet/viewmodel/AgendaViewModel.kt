package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Cita
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Agenda diaria del veterinario. */
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now()) // por defecto: hoy
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    /** Chips de los proximos dias. */
    val dias: List<LocalDate> = (0L..6L).map { LocalDate.now().plusDays(it) }

    /** Citas del dia elegido, ordenadas por hora (lo hace la consulta del DAO). */
    @OptIn(ExperimentalCoroutinesApi::class)
    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { repo.observarCitasPorFecha(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seleccionar(fecha: LocalDate) { _fechaSeleccionada.value = fecha }

    /** PENDIENTE -> CONFIRMADA (cualquier otro estado no cambia). */
    fun confirmar(cita: Cita) {
        if (cita.estado != EstadoCita.PENDIENTE.name) return
        viewModelScope.launch { repo.cambiarEstadoCita(cita.id, EstadoCita.CONFIRMADA) }
    }

    /** Desde PENDIENTE o CONFIRMADA se puede cancelar; ATENDIDA y CANCELADA son finales. */
    fun cancelar(cita: Cita) {
        val activa = cita.estado == EstadoCita.PENDIENTE.name || cita.estado == EstadoCita.CONFIRMADA.name
        if (!activa) return
        viewModelScope.launch { repo.cambiarEstadoCita(cita.id, EstadoCita.CANCELADA) }
    }
}
