package com.gutierrez.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

/** Selector de hora. Si esta [ocupada] se ve gris y tachada y no se puede tocar. */
@Composable
fun HoraChip(
    hora: String,
    seleccionada: Boolean,
    ocupada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        enabled = !ocupada,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = when {
            ocupada -> colores.surfaceVariant
            seleccionada -> colores.primary
            else -> colores.surface
        },
        border = if (seleccionada || ocupada) null else BorderStroke(1.dp, colores.outline)
    ) {
        Text(
            text = hora,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textDecoration = if (ocupada) TextDecoration.LineThrough else TextDecoration.None,
            color = when {
                ocupada -> colores.onSurfaceVariant.copy(alpha = 0.6f)
                seleccionada -> colores.onPrimary
                else -> colores.onSurface
            }
        )
    }
}
