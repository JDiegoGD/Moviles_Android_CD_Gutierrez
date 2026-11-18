package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.HorarioAtencion
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.Horarios
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Pantalla donde el veterinario edita su horario de atencion. */
class HorarioViewModel(private val repo: VetRepository) : ViewModel() {

    val horario: StateFlow<List<HorarioAtencion>> = repo.observarHorario()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Limites del dia de atencion: de 06:00 a 22:00
    private val minimo = 6 * 60
    private val maximo = 22 * 60

    private fun guardar(h: HorarioAtencion) {
        viewModelScope.launch { repo.guardarHorario(h) }
    }

    /** Mantiene un valor dentro de [min, max] (sin lanzar error si min > max). */
    private fun limitar(valor: Int, min: Int, max: Int): Int = maxOf(min, minOf(max, valor))

    fun cambiarAbierto(h: HorarioAtencion, abierto: Boolean) = guardar(h.copy(abierto = abierto))

    // delta = +30 o -30 minutos
    fun moverApertura(h: HorarioAtencion, delta: Int) {
        val nuevo = limitar(Horarios.aMinutos(h.apertura) + delta, minimo, Horarios.aMinutos(h.cierre) - 30)
        guardar(h.copy(apertura = Horarios.aTexto(nuevo)))
    }

    fun moverCierre(h: HorarioAtencion, delta: Int) {
        val nuevo = limitar(Horarios.aMinutos(h.cierre) + delta, Horarios.aMinutos(h.apertura) + 30, maximo)
        guardar(h.copy(cierre = Horarios.aTexto(nuevo)))
    }

    fun moverDescansoInicio(h: HorarioAtencion, delta: Int) {
        val nuevo = limitar(
            Horarios.aMinutos(h.descansoInicio) + delta,
            Horarios.aMinutos(h.apertura), Horarios.aMinutos(h.descansoFin)
        )
        guardar(h.copy(descansoInicio = Horarios.aTexto(nuevo)))
    }

    fun moverDescansoFin(h: HorarioAtencion, delta: Int) {
        val nuevo = limitar(
            Horarios.aMinutos(h.descansoFin) + delta,
            Horarios.aMinutos(h.descansoInicio), Horarios.aMinutos(h.cierre)
        )
        guardar(h.copy(descansoFin = Horarios.aTexto(nuevo)))
    }
}
