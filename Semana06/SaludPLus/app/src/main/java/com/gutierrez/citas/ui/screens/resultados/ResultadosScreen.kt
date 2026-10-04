package com.gutierrez.citas.ui.screens.resultados

import androidx.compose.runtime.Composable
import com.gutierrez.citas.navigation.Rutas
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 13 · Resultados (reto extra)
// TODO: P13-1  Crear su propio modelo (data class Resultado) en data/model: examen, fecha y estado.
// TODO: P13-2  Agregar al Repositorio una lista fija de resultados de ejemplo.
// TODO: P13-3  Mostrarlos en una LazyColumn con una etiqueta de estado ("Disponible" / "En proceso").
// TODO: P13-4  Barra inferior (NavigationBar) con la pestaña "Resultados" marcada; usa onNavegar(ruta).
// TODO: P13-5  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun ResultadosScreen(
    onNavegar: (String) -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Resultados",
        acciones = listOf("Barra inferior: Inicio" to { onNavegar(Rutas.HOME) })
    )
}
