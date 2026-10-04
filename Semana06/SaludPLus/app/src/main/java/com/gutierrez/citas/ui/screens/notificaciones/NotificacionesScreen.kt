package com.gutierrez.citas.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 14 · Notificaciones (reto extra)
// TODO: P14-1  Con map sobre Repositorio.citasDelUsuario() generar un aviso por cita
//              (por ejemplo: "Tu cita con <médico> es el <fecha> a las <hora>").
// TODO: P14-2  Mostrar los avisos en una LazyColumn; si no hay citas, un mensaje de lista vacía.
// TODO: P14-3  Flecha de volver -> onAtras()
// TODO: P14-4  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun NotificacionesScreen(
    onAtras: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Notificaciones",
        acciones = listOf("Atrás" to onAtras)
    )
}
