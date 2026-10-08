package com.gutierrez.biblioteca.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.components.ItemCard
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla de favoritos. Solo observa el estado del ViewModel. */
@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    FavoritosContenido(
        estado = estado,
        onItemClick = onItemClick,
        onQuitarFavorito = viewModel::quitarFavorito,
        onVolver = onVolver,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y de probar. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosContenido(
    estado: UiState<List<Item>>,
    onItemClick: (Int) -> Unit,
    onQuitarFavorito: (Int) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no tienes favoritos.")
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
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
            onItemClick = {},
            onQuitarFavorito = {},
            onVolver = {}
        )
    }
}
