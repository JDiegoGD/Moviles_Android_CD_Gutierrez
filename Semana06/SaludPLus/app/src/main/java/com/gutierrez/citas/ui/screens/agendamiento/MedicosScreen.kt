package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 5 · Médicos
// TODO: P5-1  Mostrar el nombre de la especialidad con Repositorio.obtenerEspecialidad(especialidadId).
// TODO: P5-2  Buscador por nombre con Repositorio.buscarMedicos(especialidadId, texto).
// TODO: P5-3  LazyColumn con los médicos ya ordenados por calificación (de mayor a menor):
//             foto, nombre, estrellas, años de experiencia, reseñas y disponibilidad.
// TODO: P5-4  Al tocar un médico -> onMedico(id)   |   flecha de volver -> onAtras()
// TODO: P5-5  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun MedicosScreen(
    especialidadId: Int,
    onAtras: () -> Unit,
    onMedico: (Int) -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Médicos (especialidad $especialidadId)",
        acciones = listOf(
            "Elegir el médico 1" to { onMedico(1) },
            "Atrás" to onAtras
        )
    )
}
