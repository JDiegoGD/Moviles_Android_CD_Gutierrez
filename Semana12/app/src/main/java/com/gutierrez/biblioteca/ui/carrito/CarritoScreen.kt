package com.gutierrez.biblioteca.ui.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gutierrez.biblioteca.domain.model.LineaCarrito
import com.gutierrez.biblioteca.ui.components.BarraInferior
import com.gutierrez.biblioteca.ui.components.EstadoVacio
import com.gutierrez.biblioteca.ui.components.Pestana
import com.gutierrez.biblioteca.ui.components.formatearPrecio
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla del carrito de compras. */
@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel,
    onInicio: () -> Unit,
    onFavoritos: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val cantidadCarrito by viewModel.cantidadCarrito.collectAsStateWithLifecycle()
    CarritoContenido(
        estado = estado,
        cantidadCarrito = cantidadCarrito,
        onAumentar = viewModel::aumentar,
        onDisminuir = viewModel::disminuir,
        onEliminar = viewModel::eliminar,
        onFinalizarCompra = viewModel::vaciar,
        onInicio = onInicio,
        onFavoritos = onFavoritos,
        onCarrito = {},
        onCerrarSesion = onCerrarSesion,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y probar. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoContenido(
    estado: CarritoUiState,
    cantidadCarrito: Int,
    onAumentar: (Int) -> Unit,
    onDisminuir: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onFinalizarCompra: () -> Unit,
    onInicio: () -> Unit,
    onFavoritos: () -> Unit,
    onCarrito: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarDialogoCompra by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras", fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            Column {
                if (estado.lineas.isNotEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total (${estado.totalUnidades} ${if (estado.totalUnidades == 1) "unidad" else "unidades"}):",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = formatearPrecio(estado.totalDinero),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { mostrarDialogoCompra = true },
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                Text(
                                    text = "Finalizar compra",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
                BarraInferior(
                    seleccionada = Pestana.CARRITO,
                    cantidadCarrito = cantidadCarrito,
                    onInicio = onInicio,
                    onFavoritos = onFavoritos,
                    onCarrito = onCarrito,
                    onSalir = onCerrarSesion
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (estado.lineas.isEmpty()) {
                EstadoVacio(
                    icono = Icons.Filled.ShoppingCart,
                    titulo = "Tu carrito está vacío",
                    mensaje = "Explora el catálogo y agrega productos para comenzar."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(estado.lineas, key = { it.productoId }) { linea ->
                        ItemLineaCarrito(
                            linea = linea,
                            onAumentar = { onAumentar(linea.productoId) },
                            onDisminuir = { onDisminuir(linea.productoId) },
                            onEliminar = { onEliminar(linea.productoId) }
                        )
                    }
                }
            }
        }
    }

    if (mostrarDialogoCompra) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCompra = false },
            title = { Text("¡Compra simulada!") },
            text = { Text("Su compra se ha procesado con éxito (simulación local). ¡Gracias por su preferencia!") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoCompra = false
                        onFinalizarCompra()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
private fun ItemLineaCarrito(
    linea: LineaCarrito,
    onAumentar: () -> Unit,
    onDisminuir: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (linea.imagenUrl != null) {
                    AsyncImage(
                        model = linea.imagenUrl,
                        contentDescription = linea.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = linea.titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatearPrecio(linea.precio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            IconButton(onClick = onDisminuir, modifier = Modifier.size(32.dp)) {
                                Text(text = "−", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "${linea.cantidad}",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(onClick = onAumentar, modifier = Modifier.size(32.dp)) {
                                Text(text = "+", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CarritoContenidoPreview() {
    BibliotecaTheme {
        CarritoContenido(
            estado = CarritoUiState(
                lineas = listOf(
                    LineaCarrito(1, "Essence Mascara Lash Princess", null, 9.99, 2),
                    LineaCarrito(2, "Apple MacBook Pro", null, 1999.99, 1)
                ),
                totalUnidades = 3,
                totalDinero = 2019.97
            ),
            cantidadCarrito = 3,
            onAumentar = {},
            onDisminuir = {},
            onEliminar = {},
            onFinalizarCompra = {},
            onInicio = {},
            onFavoritos = {},
            onCarrito = {},
            onCerrarSesion = {}
        )
    }
}
