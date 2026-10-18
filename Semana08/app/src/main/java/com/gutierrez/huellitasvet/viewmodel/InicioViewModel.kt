package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.Formato
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

/** Datos de la pantalla de inicio del dueño: sus mascotas y su proxima cita. */
class InicioViewModel(
    repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Proxima cita = la primera PENDIENTE o CONFIRMADA con fecha y hora desde ahora.
     * La regla se calcula aqui (en el ViewModel), no en la pantalla ni en el DAO.
     */
    val proximaCita: StateFlow<CitaConDetalle?> = repo.observarCitasDeDueno(duenoId)
        .map { citas ->
            val ahora = LocalDateTime.now()
            citas
                .filter {
                    (it.cita.estado == EstadoCita.PENDIENTE.name ||
                        it.cita.estado == EstadoCita.CONFIRMADA.name) &&
                        !Formato.fechaHoraDe(it.cita.fecha, it.cita.hora).isBefore(ahora)
                }
                .minByOrNull { Formato.fechaHoraDe(it.cita.fecha, it.cita.hora) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
