package com.gutierrez.biblioteca.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.components.formatearPrecio
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme
import com.gutierrez.biblioteca.ui.theme.EstrellaAmarilla

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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Detalle", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (estado is UiState.Exito) {
                Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 8.dp) {
                    Button(
                        onClick = onAlternarFavorito,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(52.dp)
                    ) {
                        Icon(
                            imageVector = if (estado.datos.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (estado.datos.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
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
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (item.imagenUrl != null) {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = item.titulo,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().padding(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(72.dp)
                )
            }
        }
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            if (item.subtitulo.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = item.subtitulo,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(text = item.titulo, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                item.precio?.let {
                    Text(
                        text = formatearPrecio(it),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item.valoracion?.let {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = EstrellaAmarilla,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "%.1f".format(it),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
            if (item.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Descripción", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
                    descripcion = "Descripción de ejemplo del producto.",
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
