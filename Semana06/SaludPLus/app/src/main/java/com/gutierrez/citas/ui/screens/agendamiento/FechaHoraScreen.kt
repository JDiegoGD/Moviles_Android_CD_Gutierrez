package com.gutierrez.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 6 · Fecha y hora
// TODO: P6-1  Datos del médico arriba (Repositorio.obtenerMedico(medicoId)).
// TODO: P6-2  Fila de días para elegir; guardar el día elegido en un estado.
// TODO: P6-3  LazyVerticalGrid con Repositorio.horariosDisponibles(medicoId, fecha). Al cambiar
//             de día, los horarios se recalculan y la hora elegida se reinicia.
// TODO: P6-4  Marcar visualmente la hora seleccionada.
// TODO: P6-5  El botón "Continuar" solo se habilita con día Y hora elegidos -> onContinuar(fecha, hora)
// TODO: P6-6  Flecha de volver -> onAtras()
// TODO: P6-7  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onAtras: () -> Unit,
    onContinuar: (fecha: String, hora: String) -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Fecha y hora (médico $medicoId)",
        acciones = listOf(
            "Continuar con 2026-10-12 a las 09:00" to { onContinuar("2026-10-12", "09:00") },
            "Atrás" to onAtras
        )
    )
}
