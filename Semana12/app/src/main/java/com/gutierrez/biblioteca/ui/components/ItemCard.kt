package com.gutierrez.biblioteca.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gutierrez.biblioteca.domain.model.Item

/** Tarjeta de un libro con miniatura, titulo, subtitulo y corazon de favorito. */
@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val miniatura = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
            if (item.imagenUrl != null) {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = item.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = miniatura
                )
            } else {
                Box(
                    modifier = miniatura.background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.titulo.take(1),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.subtitulo,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            BotonFavorito(esFavorito = item.esFavorito, onClick = onAlternarFavorito)
        }
    }
}
