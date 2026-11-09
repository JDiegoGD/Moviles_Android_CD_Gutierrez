package com.gutierrez.biblioteca.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Mensaje de error con un boton opcional para reintentar; el reintento lo decide el ViewModel. */
@Composable
fun ErrorView(
    mensaje: String,
    onReintentar: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    EstadoVacio(
        icono = Icons.Filled.Warning,
        titulo = "Algo salió mal",
        mensaje = mensaje,
        textoBoton = if (onReintentar != null) "Reintentar" else null,
        onBoton = onReintentar,
        modifier = modifier
    )
}
