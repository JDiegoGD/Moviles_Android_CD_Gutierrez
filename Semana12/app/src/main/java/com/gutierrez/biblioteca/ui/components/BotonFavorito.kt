package com.gutierrez.biblioteca.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Corazon para marcar o desmarcar un favorito; lo comparten la lista, el detalle y Favoritos. */
@Composable
fun BotonFavorito(
    esFavorito: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
            tint = if (esFavorito) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
