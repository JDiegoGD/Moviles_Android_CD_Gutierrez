package com.gutierrez.biblioteca.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.components.BannerAviso
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.components.ItemCard
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla de lista. Solo observa el estado del ViewModel y le avisa de los clics. */
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit,
    onVerFavoritos: () -> Unit,
    onVerAdmin: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val carrera by viewModel.carreraSeleccionada.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    ListaContenido(
        estado = estado,
        carreraSeleccionada = carrera,
        aviso = aviso,
        onCarreraSeleccionada = viewModel::seleccionar,
        onReintentar = viewModel::reintentar,
        onItemClick = onItemClick,
        onAlternarFavorito = viewModel::alternarFavorito,
        esAdmin = viewModel.esAdmin,
        onVerFavoritos = onVerFavoritos,
        onVerAdmin = onVerAdmin,
        onCerrarSesion = onCerrarSesion,
        modifier = modifier
    )
}

/** Contenido sin ViewModel: facil de previsualizar y de probar. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContenido(
    estado: UiState<List<Item>>,
    carreraSeleccionada: Carrera,
    aviso: String?,
    onCarreraSeleccionada: (Carrera) -> Unit,
    onReintentar: () -> Unit,
    onItemClick: (Int) -> Unit,
    onAlternarFavorito: (Int) -> Unit,
    esAdmin: Boolean,
    onVerFavoritos: () -> Unit,
    onVerAdmin: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Biblioteca") },
                actions = {
                    if (esAdmin) {
                        IconButton(onClick = onVerAdmin) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Panel Admin"
                            )
                        }
                    }
                    IconButton(onClick = onVerFavoritos) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Ver favoritos"
                        )
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            FiltroCarreras(
                seleccionada = carreraSeleccionada,
                onSeleccionar = onCarreraSeleccionada
            )
            if (aviso != null) {
                BannerAviso(mensaje = aviso, onReintentar = onReintentar)
            }
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (estado) {
                    is UiState.Cargando -> Cargando()
                    is UiState.Error -> ErrorView(mensaje = estado.mensaje, onReintentar = onReintentar)
                    is UiState.Exito -> ListaDeItems(estado.datos, onItemClick, onAlternarFavorito, onReintentar)
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
private fun ListaDeItems(
    items: List<Item>,
    onItemClick: (Int) -> Unit,
    onAlternarFavorito: (Int) -> Unit,
    onActualizar: () -> Unit
) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No hay libros en esta carrera.")
                Button(onClick = onActualizar, modifier = Modifier.padding(top = 12.dp)) {
                    Text("Actualizar")
                }
            }
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
                onAlternarFavorito = { onAlternarFavorito(item.id) }
            )
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
                    Item(1, "Essence Mascara Lash Princess", "Essence", null, esFavorito = true),
                    Item(2, "Apple MacBook Pro 14 Inch Space Grey", "Apple", null)
                )
            ),
            carreraSeleccionada = Carrera.DISENO_DESARROLLO_SOFTWARE,
            aviso = "Sin conexión: mostrando datos guardados.",
            onCarreraSeleccionada = {},
            onReintentar = {},
            onItemClick = {},
            onAlternarFavorito = {},
            esAdmin = true,
            onVerFavoritos = {},
            onVerAdmin = {},
            onCerrarSesion = {}
        )
    }
}
