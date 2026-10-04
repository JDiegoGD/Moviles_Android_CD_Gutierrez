package com.gutierrez.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 12 · Detalle de cita (reto extra)
// TODO: P12-1  Con Repositorio.obtenerCita(citaId) mostrar médico, especialidad, fecha y hora.
// TODO: P12-2  Botón "Cancelar cita" que abre un AlertDialog de confirmación.
// TODO: P12-3  Al aceptar: Repositorio.cancelarCita(citaId) y luego onCancelada().
// TODO: P12-4  Flecha de volver -> onAtras()
// TODO: P12-5  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onAtras: () -> Unit,
    onCancelada: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Detalle de la cita $citaId",
        acciones = listOf(
            "Cancelar cita" to onCancelada,
            "Atrás" to onAtras
        )
    )
}
