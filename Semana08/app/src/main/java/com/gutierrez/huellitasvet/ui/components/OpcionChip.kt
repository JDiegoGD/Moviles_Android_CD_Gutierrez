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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Chip de seleccion simple (especie, sexo, duracion, filtros...). */
@Composable
fun OpcionChip(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = if (seleccionada) colores.primary else colores.surface,
        border = if (seleccionada) null else BorderStroke(1.dp, colores.outline)
    ) {
        Text(
            texto,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge,
            color = if (seleccionada) colores.onPrimary else colores.onSurface
        )
    }
}
