package com.gutierrez.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 10 · Mis citas (no viene en el diseño: hay que dibujarla con el mismo estilo)
// TODO: P10-1  LazyColumn con Repositorio.citasDelUsuario(): médico, especialidad, fecha y hora.
// TODO: P10-2  Si la lista está vacía, mostrar un mensaje y un botón "Agendar cita" -> onAgendar()
// TODO: P10-3  Al tocar una cita -> onCita(id)
// TODO: P10-4  Barra inferior (NavigationBar) con la pestaña "Citas" marcada; usa onNavegar(ruta).
// TODO: P10-5  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun MisCitasScreen(
    onNavegar: (String) -> Unit,
    onAgendar: () -> Unit,
    onCita: (Int) -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Mis citas",
        acciones = listOf(
            "Agendar cita" to onAgendar,
            "Ver el detalle de la cita 1" to { onCita(1) },
            "Barra inferior: Inicio" to { onNavegar(Rutas.HOME) }
        )
    )
}
