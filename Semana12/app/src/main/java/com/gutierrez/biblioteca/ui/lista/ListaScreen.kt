package com.gutierrez.biblioteca.ui.lista

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gutierrez.biblioteca.domain.model.Categoria
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.components.BannerAviso
import com.gutierrez.biblioteca.ui.components.BannerCatalogo
import com.gutierrez.biblioteca.ui.components.BarraInferior
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.components.EstadoVacio
import com.gutierrez.biblioteca.ui.components.ItemCard
import com.gutierrez.biblioteca.ui.components.Pestana
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
    val categoria by viewModel.categoriaSeleccionada.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    ListaContenido(
        estado = estado,
        categoriaSeleccionada = categoria,
        aviso = aviso,
        onCategoriaSeleccionada = viewModel::seleccionar,
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
    categoriaSeleccionada: Categoria,
    aviso: String?,
    onCategoriaSeleccionada: (Categoria) -> Unit,
    onReintentar: () -> Unit,
    onItemClick: (Int) -> Unit,
    onAlternarFavorito: (Int) -> Unit,
    esAdmin: Boolean,
    onVerFavoritos: () -> Unit,
    onVerAdmin: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    // La busqueda filtra la lista ya cargada; sobrevive a rotaciones.
    var consulta by rememberSaveable { mutableStateOf("") }
    // Compartido para que la fila de categorias no vuelva al inicio al recargar.
    val posicionCategorias = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                        Text(
                            text = "Catálogo",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if (esAdmin) {
                        IconButton(onClick = onVerAdmin) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Panel Admin"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BarraInferior(
                seleccionada = Pestana.INICIO,
                onInicio = {},
                onFavoritos = onVerFavoritos,
                onSalir = onCerrarSesion
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Buscador(
                consulta = consulta,
                onConsultaCambio = { consulta = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            if (aviso != null) {
                BannerAviso(mensaje = aviso, onReintentar = onReintentar)
            }
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (estado) {
                    is UiState.Cargando -> Column {
                        Categorias(categoriaSeleccionada, onCategoriaSeleccionada, posicionCategorias)
                        Cargando(modifier = Modifier.weight(1f))
                    }
                    is UiState.Error -> Column {
                        Categorias(categoriaSeleccionada, onCategoriaSeleccionada, posicionCategorias)
                        ErrorView(
                            mensaje = estado.mensaje,
                            onReintentar = onReintentar,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    is UiState.Exito -> ListaDeItems(
                        items = estado.datos,
                        categoria = categoriaSeleccionada,
                        consulta = consulta,
                        onCategoriaSeleccionada = onCategoriaSeleccionada,
                        posicionCategorias = posicionCategorias,
                        onItemClick = onItemClick,
                        onAlternarFavorito = onAlternarFavorito,
                        onActualizar = onReintentar
                    )
                }
            }
        }
    }
}

/** Buscador relleno de gris claro y sin borde, como en las apps de delivery. */
@Composable
private fun Buscador(
    consulta: String,
    onConsultaCambio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = consulta,
        onValueChange = onConsultaCambio,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("¿Qué estás buscando?") },
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (consulta.isNotEmpty()) {
                IconButton(onClick = { onConsultaCambio("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Borrar búsqueda")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

/** Fila de categorias: mosaico con emoji y nombre debajo. */
@Composable
private fun Categorias(
    seleccionada: Categoria,
    onSeleccionar: (Categoria) -> Unit,
    estado: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyRow(
        state = estado,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(Categoria.entries, key = { it.name }) { categoria ->
            MosaicoCategoria(
                categoria = categoria,
                seleccionada = categoria == seleccionada,
                onClick = { onSeleccionar(categoria) }
            )
        }
    }
}

@Composable
private fun MosaicoCategoria(
    categoria: Categoria,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    val primario = MaterialTheme.colorScheme.primary
    Column(
        modifier = Modifier.width(76.dp).clip(RoundedCornerShape(16.dp)).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (seleccionada) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
                .border(
                    border = BorderStroke(
                        width = if (seleccionada) 2.dp else 1.dp,
                        color = if (seleccionada) primario else MaterialTheme.colorScheme.outline
                    ),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = categoria.emoji, fontSize = 30.sp)
        }
        Text(
            text = categoria.nombre,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
            color = if (seleccionada) primario else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun ListaDeItems(
    items: List<Item>,
    categoria: Categoria,
    consulta: String,
    onCategoriaSeleccionada: (Categoria) -> Unit,
    posicionCategorias: LazyListState,
    onItemClick: (Int) -> Unit,
    onAlternarFavorito: (Int) -> Unit,
    onActualizar: () -> Unit
) {
    val filtrados = if (consulta.isBlank()) {
        items
    } else {
        items.filter {
            it.titulo.contains(consulta, ignoreCase = true) ||
                it.subtitulo.contains(consulta, ignoreCase = true)
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (consulta.isBlank()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                BannerCatalogo(modifier = Modifier.padding(top = 8.dp))
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            // La fila de categorias ocupa todo el ancho, por eso se compensa el margen de la cuadricula.
            Categorias(
                seleccionada = categoria,
                onSeleccionar = onCategoriaSeleccionada,
                estado = posicionCategorias,
                modifier = Modifier.layoutSinMargen()
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (consulta.isBlank()) "Populares en ${categoria.nombre}" else "Resultados",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (filtrados.size == 1) "1 producto" else "${filtrados.size} productos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        when {
            items.isEmpty() -> item(span = { GridItemSpan(maxLineSpan) }) {
                EstadoVacio(
                    icono = Icons.Filled.ShoppingCart,
                    titulo = "No hay productos en esta categoría",
                    mensaje = "Prueba con otra categoría o vuelve a actualizar.",
                    textoBoton = "Actualizar",
                    onBoton = onActualizar,
                    modifier = Modifier.height(340.dp)
                )
            }
            filtrados.isEmpty() -> item(span = { GridItemSpan(maxLineSpan) }) {
                EstadoVacio(
                    icono = Icons.Filled.Search,
                    titulo = "Sin resultados",
                    mensaje = "No encontramos productos que coincidan con \"$consulta\".",
                    modifier = Modifier.height(340.dp)
                )
            }
            else -> items(filtrados, key = { it.id }) { item ->
                ItemCard(
                    item = item,
                    onClick = { onItemClick(item.id) },
                    onAlternarFavorito = { onAlternarFavorito(item.id) }
                )
            }
        }
    }
}

/** Quita el margen horizontal de 16 dp de la cuadricula para que la fila de categorias llegue al borde. */
private fun Modifier.layoutSinMargen(): Modifier = this.then(
    Modifier.layout { medida, restricciones ->
        val extra = 32.dp.roundToPx()
        val colocable = medida.measure(
            restricciones.copy(
                minWidth = restricciones.maxWidth + extra,
                maxWidth = restricciones.maxWidth + extra
            )
        )
        layout(restricciones.maxWidth, colocable.height) { colocable.place(-extra / 2, 0) }
    }
)

@Preview(showBackground = true)
@Composable
private fun ListaContenidoPreview() {
    BibliotecaTheme {
        ListaContenido(
            estado = UiState.Exito(
                listOf(
                    Item(1, "Essence Mascara Lash Princess", "Essence", null, esFavorito = true, precio = 9.99, valoracion = 4.9),
                    Item(2, "Apple MacBook Pro 14 Inch Space Grey", "Apple", null, precio = 1999.99, valoracion = 4.5)
                )
            ),
            categoriaSeleccionada = Categoria.LAPTOPS,
            aviso = "Sin conexión: mostrando datos guardados.",
            onCategoriaSeleccionada = {},
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
