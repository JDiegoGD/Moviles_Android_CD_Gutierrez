package com.gutierrez.biblioteca.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Pantalla centrada con icono, titulo, mensaje y un boton opcional (vacio, sin resultados, errores). */
@Composable
fun EstadoVacio(
    icono: ImageVector,
    titulo: String,
    mensaje: String? = null,
    textoBoton: String? = null,
    onBoton: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(44.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = titulo, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        if (mensaje != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        if (textoBoton != null && onBoton != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onBoton) { Text(textoBoton) }
        }
    }
}
