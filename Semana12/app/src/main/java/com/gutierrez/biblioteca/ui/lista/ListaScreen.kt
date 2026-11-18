package com.gutierrez.biblioteca.ui.lista

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.gutierrez.biblioteca.domain.model.OpcionesLista
import com.gutierrez.biblioteca.domain.model.Orden
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.components.BannerAviso
import com.gutierrez.biblioteca.ui.components.BannerCatalogo
import com.gutierrez.biblioteca.ui.components.BarraInferior
import com.gutierrez.biblioteca.ui.components.Cargando
import com.gutierrez.biblioteca.ui.components.ErrorView
import com.gutierrez.biblioteca.ui.components.EstadoVacio
import com.gutierrez.biblioteca.ui.components.ItemCard
import com.gutierrez.biblioteca.ui.components.Pestana
import com.gutierrez.biblioteca.ui.components.TarjetaEsqueleto
import com.gutierrez.biblioteca.ui.components.formatearPrecio
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

/** Pantalla de lista. Solo observa el estado del ViewModel y le avisa de los clics. */
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit,
    onVerFavoritos: () -> Unit,
    onVerCarrito: () -> Unit,
    onVerAdmin: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.itemsFiltrados.collectAsStateWithLifecycle()
    val categoria by viewModel.categoriaSeleccionada.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val cantidadCarrito by viewModel.cantidadCarrito.collectAsStateWithLifecycle()
    val opciones by viewModel.opciones.collectAsStateWithLifecycle()
    val consulta by viewModel.consulta.collectAsStateWithLifecycle()
    val rangoPrecioReal by viewModel.rangoPrecioReal.collectAsStateWithLifecycle()
    val refrescando by viewModel.refrescando.collectAsStateWithLifecycle()

    ListaContenido(
        estado = estado,
        categoriaSeleccionada = categoria,
        aviso = aviso,
        cantidadCarrito = cantidadCarrito,
        opciones = opciones,
        consulta = consulta,
        rangoPrecioReal = rangoPrecioReal,
        refrescando = refrescando,
        onRefrescar = viewModel::refrescar,
        onCategoriaSeleccionada = viewModel::seleccionar,
        onReintentar = viewModel::reintentar,
        onItemClick = onItemClick,
        onAlternarFavorito = viewModel::alternarFavorito,
        onActualizarOpciones = viewModel::actualizarOpciones,
        onActualizarConsulta = viewModel::actualizarConsulta,
        onLimpiarFiltros = viewModel::limpiarFiltros,
        esAdmin = viewModel.esAdmin,
        onVerFavoritos = onVerFavoritos,
        onVerCarrito = onVerCarrito,
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
    cantidadCarrito: Int,
    opciones: OpcionesLista,
    consulta: String,
    rangoPrecioReal: Pair<Float, Float>,
    refrescando: Boolean,
    onRefrescar: () -> Unit,
    onCategoriaSeleccionada: (Categoria) -> Unit,
    onReintentar: () -> Unit,
    onItemClick: (Int) -> Unit,
    onAlternarFavorito: (Int) -> Unit,
    onActualizarOpciones: (OpcionesLista) -> Unit,
    onActualizarConsulta: (String) -> Unit,
    onLimpiarFiltros: () -> Unit,
    esAdmin: Boolean,
    onVerFavoritos: () -> Unit,
    onVerCarrito: () -> Unit,
    onVerAdmin: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val posicionCategorias = rememberLazyListState()
    var mostrarBottomSheet by remember { mutableStateOf(false) }

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
                cantidadCarrito = cantidadCarrito,
                onInicio = {},
                onFavoritos = onVerFavoritos,
                onCarrito = onVerCarrito,
                onSalir = onCerrarSesion
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Buscador(
                    consulta = consulta,
                    onConsultaCambio = onActualizarConsulta,
                    modifier = Modifier.weight(1f)
                )
                BadgedBox(
                    badge = {
                        if (opciones.cantidadFiltrosActivos > 0) {
                            Badge { Text("${opciones.cantidadFiltrosActivos}") }
                        }
                    }
                ) {
                    IconButton(
                        onClick = { mostrarBottomSheet = true },
                        modifier = Modifier
                            .size(52.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(26.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(26.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Ordenar y filtrar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (aviso != null) {
                BannerAviso(mensaje = aviso, onReintentar = onReintentar)
            }

            PullToRefreshBox(
                isRefreshing = refrescando,
                onRefresh = onRefrescar,
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (estado) {
                        is UiState.Cargando -> ListaDeEsqueletos(
                            categoria = categoriaSeleccionada,
                            onCategoriaSeleccionada = onCategoriaSeleccionada,
                            posicionCategorias = posicionCategorias
                        )
                        is UiState.Error -> Column {
                            Categorias(categoriaSeleccionada, onCategoriaSeleccionada, posicionCategorias)
                            ErrorView(
                                mensaje = estado.mensaje,
                                onReintentar = onReintentar,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is UiState.Exito -> ListaDeItems(
                            items = estado.datos,
                            categoria = categoriaSeleccionada,
                            consulta = consulta,
                            tieneFiltrosActivos = opciones.hayFiltrosActivos,
                            onCategoriaSeleccionada = onCategoriaSeleccionada,
                            posicionCategorias = posicionCategorias,
                            onItemClick = onItemClick,
                            onAlternarFavorito = onAlternarFavorito,
                            onActualizar = onReintentar,
                            onLimpiarFiltros = onLimpiarFiltros
                        )
                    }
                }
            }
        }
    }

    if (mostrarBottomSheet) {
        FiltrosBottomSheet(
            opcionesActuales = opciones,
            rangoReal = rangoPrecioReal,
            onDismiss = { mostrarBottomSheet = false },
            onAplicar = { nuevasOpciones ->
                onActualizarOpciones(nuevasOpciones)
                mostrarBottomSheet = false
            },
            onLimpiar = {
                onLimpiarFiltros()
                mostrarBottomSheet = false
            }
        )
    }
}

@Composable
private fun ListaDeEsqueletos(
    categoria: Categoria,
    onCategoriaSeleccionada: (Categoria) -> Unit,
    posicionCategorias: LazyListState
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            BannerCatalogo(modifier = Modifier.padding(top = 8.dp))
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
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
                    text = "Populares en ${categoria.nombre}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        items(6) {
            TarjetaEsqueleto()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltrosBottomSheet(
    opcionesActuales: OpcionesLista,
    rangoReal: Pair<Float, Float>,
    onDismiss: () -> Unit,
    onAplicar: (OpcionesLista) -> Unit,
    onLimpiar: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var ordenTemp by remember { mutableStateOf(opcionesActuales.orden) }
    
    val minVal = rangoReal.first
    val maxVal = if (rangoReal.second > rangoReal.first) rangoReal.second else rangoReal.first + 10f
    
    var rangoTemp by remember {
        mutableStateOf(
            (opcionesActuales.precioMinimo ?: minVal).coerceIn(minVal, maxVal)..(opcionesActuales.precioMaximo ?: maxVal).coerceIn(minVal, maxVal)
        )
    }
    var soloFavoritosTemp by remember { mutableStateOf(opcionesActuales.soloFavoritos) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            Text(
                text = "Ordenar y filtrar",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ordenar por",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Orden.entries.forEach { orden ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { ordenTemp = orden }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = ordenTemp == orden,
                            onClick = { ordenTemp = orden }
                        )
                        Text(
                            text = orden.nombre,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Precio",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${formatearPrecio(rangoTemp.start.toDouble())} - ${formatearPrecio(rangoTemp.endInclusive.toDouble())}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            RangeSlider(
                value = rangoTemp,
                onValueChange = { rangoTemp = it },
                valueRange = minVal..maxVal
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Solo mis favoritos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Switch(
                    checked = soloFavoritosTemp,
                    onCheckedChange = { soloFavoritosTemp = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onLimpiar,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Limpiar", style = MaterialTheme.typography.titleMedium)
                }
                Button(
                    onClick = {
                        onAplicar(
                            OpcionesLista(
                                orden = ordenTemp,
                                precioMinimo = rangoTemp.start,
                                precioMaximo = rangoTemp.endInclusive,
                                soloFavoritos = soloFavoritosTemp
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Aplicar", style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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
    tieneFiltrosActivos: Boolean,
    onCategoriaSeleccionada: (Categoria) -> Unit,
    posicionCategorias: LazyListState,
    onItemClick: (Int) -> Unit,
    onAlternarFavorito: (Int) -> Unit,
    onActualizar: () -> Unit,
    onLimpiarFiltros: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (consulta.isBlank() && !tieneFiltrosActivos) {
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
                    text = if (consulta.isBlank() && !tieneFiltrosActivos) "Populares en ${categoria.nombre}" else "Resultados",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (items.size == 1) "1 producto" else "${items.size} productos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        when {
            items.isEmpty() -> item(span = { GridItemSpan(maxLineSpan) }) {
                EstadoVacio(
                    icono = if (consulta.isNotBlank() || tieneFiltrosActivos) Icons.Filled.Search else Icons.Filled.ShoppingCart,
                    titulo = if (consulta.isNotBlank() || tieneFiltrosActivos) "Sin resultados" else "No hay productos en esta categoría",
                    mensaje = if (consulta.isNotBlank() || tieneFiltrosActivos) "No encontramos productos que coincidan con tu búsqueda o filtros." else "Prueba con otra categoría o vuelve a actualizar.",
                    textoBoton = if (consulta.isNotBlank() || tieneFiltrosActivos) "Limpiar filtros" else "Actualizar",
                    onBoton = if (consulta.isNotBlank() || tieneFiltrosActivos) onLimpiarFiltros else onActualizar,
                    modifier = Modifier.height(340.dp)
                )
            }
            else -> items(items, key = { it.id }) { item ->
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
            cantidadCarrito = 3,
            opciones = OpcionesLista(),
            consulta = "",
            rangoPrecioReal = Pair(0f, 2000f),
            refrescando = false,
            onRefrescar = {},
            onCategoriaSeleccionada = {},
            onReintentar = {},
            onItemClick = {},
            onAlternarFavorito = {},
            onActualizarOpciones = {},
            onActualizarConsulta = {},
            onLimpiarFiltros = {},
            esAdmin = true,
            onVerFavoritos = {},
            onVerCarrito = {},
            onVerAdmin = {},
            onCerrarSesion = {}
        )
    }
}
