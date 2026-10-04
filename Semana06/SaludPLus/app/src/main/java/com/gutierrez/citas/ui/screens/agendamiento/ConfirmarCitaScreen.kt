package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 7 · Confirmar cita
// TODO: P7-1  Con medicoId obtener el médico y su especialidad desde el Repositorio.
// TODO: P7-2  Resumen de la cita: médico, especialidad, fecha y hora (usar FilaDato).
// TODO: P7-3  Campo opcional "Motivo de consulta".
// TODO: P7-4  "Confirmar" llama a Repositorio.agendarCita(medicoId, especialidadId, fecha, hora).
//             Si devuelve true -> onConfirmada(); si es false, avisar que el horario ya fue tomado.
// TODO: P7-5  Flecha de volver -> onAtras()
// TODO: P7-6  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onAtras: () -> Unit,
    onConfirmada: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Confirmar cita\nmédico $medicoId · $fecha · $hora",
        acciones = listOf(
            "Confirmar" to onConfirmada,
            "Atrás" to onAtras
        )
    )
}
