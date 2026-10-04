package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 9 · Cita agendada (no viene en el diseño: hay que dibujarla con el mismo estilo)
// TODO: P9-1  Ícono o ilustración de éxito y el mensaje "¡Cita agendada!".
// TODO: P9-2  Resumen de la última cita creada (la más reciente de Repositorio.citasDelUsuario()).
// TODO: P9-3  Botón "Ver mis citas" -> onVerMisCitas()   |   "Ir al inicio" -> onIrInicio()
// TODO: P9-4  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun CitaExitosaScreen(
    onVerMisCitas: () -> Unit,
    onIrInicio: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Cita agendada",
        acciones = listOf(
            "Ver mis citas" to onVerMisCitas,
            "Ir al inicio" to onIrInicio
        )
    )
}
