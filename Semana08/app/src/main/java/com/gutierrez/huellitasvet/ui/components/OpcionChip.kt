package com.gutierrez.huellitasvet.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Chip de seleccion (especie, sexo, duracion, filtros...) con el FilterChip de Material 3. */
@Composable
fun OpcionChip(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    FilterChip(
        selected = seleccionada,
        onClick = onClick,
        label = { Text(texto) },
        modifier = modifier,
        shape = RoundedCornerShape(50),
        leadingIcon = if (seleccionada) {
            { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = colores.surface,
            labelColor = colores.onSurface,
            selectedContainerColor = colores.primary,
            selectedLabelColor = colores.onPrimary,
            selectedLeadingIconColor = colores.onPrimary
        )
    )
}
