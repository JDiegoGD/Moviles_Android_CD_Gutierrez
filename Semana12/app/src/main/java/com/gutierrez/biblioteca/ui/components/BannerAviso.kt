package com.gutierrez.biblioteca.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Franja de aviso (por ejemplo "sin conexion") con un boton para volver a intentar. */
@Composable
fun BannerAviso(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f).padding(vertical = 8.dp)
            )
            TextButton(onClick = onReintentar) {
                Text("Reintentar")
            }
        }
    }
}
