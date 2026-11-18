package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Registro clinico de una cita (diagnostico, tratamiento y peso). */
class AtenderCitaViewModel(
    private val repo: VetRepository,
    private val citaId: Int
) : ViewModel() {

    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Errores por campo: "diagnostico", "peso", "control", "general"
    private val _errores = MutableStateFlow<Map<String, String>>(emptyMap())
    val errores: StateFlow<Map<String, String>> = _errores.asStateFlow()

    private fun sePuedeAtender(estado: String) =
        estado == EstadoCita.PENDIENTE.name || estado == EstadoCita.CONFIRMADA.name

    /**
     * Valida y llama a repo.registrarAtencion (UNA transaccion): la cita queda ATENDIDA
     * y el peso de la mascota se actualiza en la misma operacion.
     */
    fun registrar(diagnostico: String, tratamiento: String, peso: Double, fechaControl: String?, onListo: () -> Unit) {
        viewModelScope.launch {
            val actual = repo.observarCita(citaId).first()
            val errores = mutableMapOf<String, String>()
            if (actual == null || !sePuedeAtender(actual.cita.estado)) {
                errores["general"] = "Solo se puede atender una cita pendiente o confirmada"
            }
            if (diagnostico.trim().length < 5) errores["diagnostico"] = "Mínimo 5 caracteres"
            if (peso <= 0.0) errores["peso"] = "Debe ser mayor que 0"
            if (fechaControl != null) {
                val ctrl = LocalDate.parse(fechaControl)
                val hoy = LocalDate.now()
                val fechaCita = LocalDate.parse(actual?.cita?.fecha ?: LocalDate.now().toString())
                if (!ctrl.isAfter(hoy) || !ctrl.isAfter(fechaCita)) {
                    errores["control"] = "Debe ser posterior a hoy y a la fecha de la cita"
                }
            }
            _errores.value = errores
            if (errores.isNotEmpty()) return@launch

            repo.registrarAtencion(citaId, diagnostico.trim(), tratamiento.trim(), peso, fechaControl)
            onListo()
        }
    }

    fun cancelar(onListo: () -> Unit) {
        viewModelScope.launch {
            val actual = repo.observarCita(citaId).first()
            if (actual != null && sePuedeAtender(actual.cita.estado)) {
                repo.cambiarEstadoCita(citaId, EstadoCita.CANCELADA)
                onListo()
            } else {
                _errores.value = mapOf("general" to "Esta cita ya no se puede cancelar")
            }
        }
    }

    fun limpiarErrores() { _errores.value = emptyMap() }
}
