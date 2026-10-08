package com.gutierrez.biblioteca.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla de lista. Solo observa el estado del ViewModel y le avisa de los clics. */
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val carrera by viewModel.carreraSeleccionada.collectAsStateWithLifecycle()
    ListaContenido(
        estado = estado,
        carreraSeleccionada = carrera,
        onCarreraSeleccionada = viewModel::seleccionar,
        onReintentar = viewModel::reintentar,
        onItemClick = onItemClick,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y de probar. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContenido(
    estado: UiState<List<Item>>,
    carreraSeleccionada: Carrera,
    onCarreraSeleccionada: (Carrera) -> Unit,
    onReintentar: () -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Biblioteca") }) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            FiltroCarreras(
                seleccionada = carreraSeleccionada,
                onSeleccionar = onCarreraSeleccionada
            )
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (estado) {
                    is UiState.Cargando -> Cargando()
                    is UiState.Error -> ErrorView(mensaje = estado.mensaje, onReintentar = onReintentar)
                    is UiState.Exito -> ListaDeItems(estado.datos, onItemClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltroCarreras(
    seleccionada: Carrera,
    onSeleccionar: (Carrera) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(Carrera.entries, key = { it.name }) { carrera ->
            FilterChip(
                selected = carrera == seleccionada,
                onClick = { onSeleccionar(carrera) },
                label = { Text(carrera.nombre) }
            )
        }
    }
}

@Composable
private fun ListaDeItems(items: List<Item>, onItemClick: (Int) -> Unit) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay libros en esta carrera.")
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.id }) { item ->
            ItemCard(item, onClick = { onItemClick(item.id) })
        }
    }
}

@Composable
private fun ItemCard(item: Item, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(12.dp),
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
            Column(modifier = Modifier.padding(start = 12.dp)) {
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaContenidoPreview() {
    BibliotecaTheme {
        ListaContenido(
            estado = UiState.Exito(
                listOf(
                    Item(1, "Essence Mascara Lash Princess", "Essence", null),
                    Item(2, "Apple MacBook Pro 14 Inch Space Grey", "Apple", null)
                )
            ),
            carreraSeleccionada = Carrera.DISENO_DESARROLLO_SOFTWARE,
            onCarreraSeleccionada = {},
            onReintentar = {},
            onItemClick = {}
        )
    }
}
