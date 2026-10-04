package com.gutierrez.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 15 · Términos y condiciones (reto extra)
// TODO: P15-1  Redactar un texto de ejemplo de términos y mostrarlo con scroll (verticalScroll)
//              o dentro de un AlertDialog.
// TODO: P15-2  Flecha de volver -> onAtras()
// TODO: P15-3  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun TerminosScreen(
    onAtras: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Términos y condiciones",
        acciones = listOf("Atrás" to onAtras)
    )
}
