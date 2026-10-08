package com.gutierrez.biblioteca.ui.detalle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.components.BotonFavorito
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla de detalle. Solo observa el estado del ViewModel. */
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    DetalleContenido(
        estado = estado,
        onVolver = onVolver,
        onReintentar = viewModel::reintentar,
        onAlternarFavorito = viewModel::alternarFavorito,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y de probar. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleContenido(
    estado: UiState<Item>,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    if (estado is UiState.Exito) {
                        BotonFavorito(
                            esFavorito = estado.datos.esFavorito,
                            onClick = onAlternarFavorito
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(mensaje = estado.mensaje, onReintentar = onReintentar)
                is UiState.Exito -> DetalleDeItem(estado.datos)
            }
        }
    }
}

@Composable
private fun DetalleDeItem(item: Item) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (item.imagenUrl != null) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(text = item.titulo, style = MaterialTheme.typography.headlineSmall)
        if (item.subtitulo.isNotBlank()) {
            Text(text = item.subtitulo, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            item.precio?.let {
                Text(
                    text = "\$%.2f".format(it),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item.valoracion?.let {
                Text(
                    text = "Valoración: %.1f".format(it),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        if (item.descripcion.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = item.descripcion, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleContenidoPreview() {
    BibliotecaTheme {
        DetalleContenido(
            estado = UiState.Exito(
                Item(
                    id = 1,
                    titulo = "Essence Mascara Lash Princess",
                    subtitulo = "Essence",
                    imagenUrl = null,
                    descripcion = "Descripción de ejemplo del libro.",
                    precio = 9.99,
                    valoracion = 4.5
                )
            ),
            onVolver = {},
            onReintentar = {},
            onAlternarFavorito = {}
        )
    }
}
