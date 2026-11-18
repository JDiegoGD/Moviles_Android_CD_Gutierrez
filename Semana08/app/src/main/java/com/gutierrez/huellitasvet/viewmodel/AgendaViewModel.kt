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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

/** Agenda diaria del veterinario. */
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now()) // por defecto: hoy
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    private val _mesVisible = MutableStateFlow(YearMonth.now())
    val mesVisible: StateFlow<YearMonth> = _mesVisible.asStateFlow()

    fun mesAnterior() {
        _mesVisible.value = _mesVisible.value.minusMonths(1)
    }

    fun mesSiguiente() {
        _mesVisible.value = _mesVisible.value.plusMonths(1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val diasConCitas: StateFlow<Set<LocalDate>> = _mesVisible
        .flatMapLatest { ym ->
            val inicio = ym.atDay(1).toString()
            val fin = ym.atEndOfMonth().toString()
            repo.observarDiasConCitas(inicio, fin).map { lista ->
                lista.map { LocalDate.parse(it) }.toSet()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    @OptIn(ExperimentalCoroutinesApi::class)
    val diasMes: StateFlow<List<LocalDate?>> = _mesVisible
        .map { ym ->
            val primerDia = ym.atDay(1)
            val diaSemana = primerDia.dayOfWeek.value // 1 (Mon) to 7 (Sun)
            val espaciosVacios = diaSemana - 1
            val diasDelMes = (1..ym.lengthOfMonth()).map { ym.atDay(it) }
            List<LocalDate?>(espaciosVacios) { null } + diasDelMes
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
