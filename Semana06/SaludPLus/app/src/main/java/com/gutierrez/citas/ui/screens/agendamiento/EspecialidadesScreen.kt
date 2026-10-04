package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 4 · Especialidades
// TODO: P4-1  Barra superior con flecha de volver -> onAtras()
// TODO: P4-2  Campo de búsqueda con un estado de texto (remember + mutableStateOf).
// TODO: P4-3  LazyColumn con Repositorio.buscarEspecialidades(texto): al escribir, la lista
//             se recalcula sola. Si no hay coincidencias, mostrar un mensaje de lista vacía.
// TODO: P4-4  Al tocar una tarjeta -> onEspecialidad(id)
// TODO: P4-5  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun EspecialidadesScreen(
    onAtras: () -> Unit,
    onEspecialidad: (Int) -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Especialidades",
        acciones = listOf(
            "Elegir la especialidad 1" to { onEspecialidad(1) },
            "Atrás" to onAtras
        )
    )
}