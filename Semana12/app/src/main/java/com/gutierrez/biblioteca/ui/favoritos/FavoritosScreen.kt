package com.gutierrez.biblioteca.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.components.BarraInferior
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.components.EstadoVacio
import com.gutierrez.biblioteca.ui.components.ItemCard
import com.gutierrez.biblioteca.ui.components.Pestana
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla de favoritos. Solo observa el estado del ViewModel. */
@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    onVolver: () -> Unit,
    onVerCarrito: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val cantidadCarrito by viewModel.cantidadCarrito.collectAsStateWithLifecycle()
    FavoritosContenido(
        estado = estado,
        cantidadCarrito = cantidadCarrito,
        onItemClick = onItemClick,
        onQuitarFavorito = viewModel::quitarFavorito,
        onVolver = onVolver,
        onVerCarrito = onVerCarrito,
        onCerrarSesion = onCerrarSesion,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y de probar. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosContenido(
    estado: UiState<List<Item>>,
    cantidadCarrito: Int,
    onItemClick: (Int) -> Unit,
    onQuitarFavorito: (Int) -> Unit,
    onVolver: () -> Unit,
    onVerCarrito: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Favoritos", fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            BarraInferior(
                seleccionada = Pestana.FAVORITOS,
                cantidadCarrito = cantidadCarrito,
                onInicio = onVolver,
                onFavoritos = {},
                onCarrito = onVerCarrito,
                onSalir = onCerrarSesion
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(mensaje = estado.mensaje)
                is UiState.Exito -> ListaDeFavoritos(estado.datos, onItemClick, onQuitarFavorito)
            }
        }
    }
}

@Composable
private fun ListaDeFavoritos(
    items: List<Item>,
    onItemClick: (Int) -> Unit,
    onQuitarFavorito: (Int) -> Unit
) {
    if (items.isEmpty()) {
        EstadoVacio(
            icono = Icons.Filled.Favorite,
            titulo = "Aún no tienes favoritos",
            mensaje = "Toca el corazón de un producto para guardarlo aquí."
        )
        return
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.id }) { item ->
            ItemCard(
                item = item,
                onClick = { onItemClick(item.id) },
                onAlternarFavorito = { onQuitarFavorito(item.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritosContenidoPreview() {
    BibliotecaTheme {
        FavoritosContenido(
            estado = UiState.Exito(
                listOf(Item(1, "Essence Mascara Lash Princess", "Essence", null, esFavorito = true))
            ),
            cantidadCarrito = 2,
            onItemClick = {},
            onQuitarFavorito = {},
            onVolver = {},
            onVerCarrito = {},
            onCerrarSesion = {}
        )
    }
}
