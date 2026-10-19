package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.ui.components.graficos.DatoGrafico
import com.gutierrez.huellitasvet.ui.theme.ColorAtendida
import com.gutierrez.huellitasvet.ui.theme.ColorCancelada
import com.gutierrez.huellitasvet.ui.theme.ColorConfirmada
import com.gutierrez.huellitasvet.ui.theme.ColorPendiente
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

/** Panel de la clinica: contadores, ingresos del mes y datos para los graficos. */
class PanelViewModel(repo: VetRepository) : ViewModel() {

    private val hoy = LocalDate.now().toString()
    private val diaUnoDelMes = LocalDate.now().withDayOfMonth(1).toString()

    private fun <T> comoStateFlow(f: kotlinx.coroutines.flow.Flow<T>, inicial: T): StateFlow<T> =
        f.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), inicial)

    val citasHoy: StateFlow<Int> = comoStateFlow(repo.observarCitasDeFecha(hoy), 0)

    private val pend = repo.observarCantidadPorEstado(EstadoCita.PENDIENTE)
    private val conf = repo.observarCantidadPorEstado(EstadoCita.CONFIRMADA)
    private val aten = repo.observarCantidadPorEstado(EstadoCita.ATENDIDA)
    private val canc = repo.observarCantidadPorEstado(EstadoCita.CANCELADA)

    val pendientes: StateFlow<Int> = comoStateFlow(pend, 0)
    val atendidas: StateFlow<Int> = comoStateFlow(aten, 0)

    /** Suma del precio de las citas ATENDIDAS desde el dia 1 del mes. */
    val ingresosMes: StateFlow<Double> = comoStateFlow(repo.observarIngresosDesde(diaUnoDelMes), 0.0)

    /** Room (conteos) -> List<DatoGrafico> para GraficoDona. */
    val porEstado: StateFlow<List<DatoGrafico>> = comoStateFlow(
        combine(pend, conf, aten, canc) { p, c, a, x ->
            listOf(
                DatoGrafico("Pendiente", p.toFloat(), ColorPendiente),
                DatoGrafico("Confirmada", c.toFloat(), ColorConfirmada),
                DatoGrafico("Atendida", a.toFloat(), ColorAtendida),
                DatoGrafico("Cancelada", x.toFloat(), ColorCancelada)
            )
        },
        emptyList()
    )

    /** Room (GROUP BY) -> List<DatoGrafico> para GraficoBarras. */
    val porServicio: StateFlow<List<DatoGrafico>> = comoStateFlow(
        repo.observarCitasPorServicio().map { lista ->
            lista.map { DatoGrafico(it.nombre.substringBefore(' '), it.total.toFloat()) }
        },
        emptyList()
    )
}
