package com.gutierrez.biblioteca.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.auth.AuthRepository
import com.gutierrez.biblioteca.data.repository.CarritoRepository
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.domain.aplicarOpciones
import com.gutierrez.biblioteca.domain.model.Categoria
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.domain.model.OpcionesLista
import com.gutierrez.biblioteca.domain.model.Rol
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.avisoDatosGuardados
import com.gutierrez.biblioteca.ui.mensajeDeError
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de la lista. Observa Room (fuente unica de verdad) y pide al
 * repositorio que la actualice desde la API; maneja opciones de orden, filtrado y refresco.
 */
class ListaViewModel(
    private val repository: ItemRepository,
    private val carritoRepository: CarritoRepository,
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow(Categoria.entries.first())
    val categoriaSeleccionada: StateFlow<Categoria> = _categoriaSeleccionada.asStateFlow()

    /** Aviso para el usuario (por ejemplo, sin conexion); null cuando no hay nada que avisar. */
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _refrescando = MutableStateFlow(false)
    val refrescando: StateFlow<Boolean> = _refrescando.asStateFlow()

    private val _opciones = MutableStateFlow(OpcionesLista())
    val opciones: StateFlow<OpcionesLista> = _opciones.asStateFlow()

    private val _consulta = MutableStateFlow("")
    val consulta: StateFlow<String> = _consulta.asStateFlow()

    val rangoPrecioReal: StateFlow<Pair<Float, Float>> = _estado
        .map { estado ->
            val items = (estado as? UiState.Exito)?.datos ?: emptyList()
            val precios = items.mapNotNull { it.precio }
            val min = precios.minOrNull()?.toFloat() ?: 0f
            val max = precios.maxOrNull()?.toFloat() ?: 1000f
            val safeMin = min
            val safeMax = if (max > min) max else min + 10f
            Pair(safeMin, safeMax)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Pair(0f, 1000f)
        )

    val itemsFiltrados: StateFlow<UiState<List<Item>>> = combine(
        _estado,
        _opciones,
        _consulta
    ) { estadoActual, opciones, consulta ->
        when (estadoActual) {
            is UiState.Cargando -> UiState.Cargando
            is UiState.Error -> estadoActual
            is UiState.Exito -> {
                val filtrados = aplicarOpciones(estadoActual.datos, opciones, consulta)
                UiState.Exito(filtrados)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Cargando
    )

    val cantidadCarrito: StateFlow<Int> = carritoRepository.observarCarrito()
        .map { lineas -> lineas.sumOf { it.cantidad } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    /** El acceso al panel Admin solo se muestra a quien ingreso con el rol ADMIN. */
    val esAdmin: Boolean = auth.sesionActual()?.rol == Rol.ADMIN

    private var trabajo: Job? = null

    init {
        cargar()
    }

    fun seleccionar(categoria: Categoria) {
        if (categoria == _categoriaSeleccionada.value) return
        _categoriaSeleccionada.value = categoria
        // Al cambiar de categoría, el orden se mantiene pero el rango de precio y "solo favoritos" se reinician.
        _opciones.value = _opciones.value.copy(
            precioMinimo = null,
            precioMaximo = null,
            soloFavoritos = false
        )
        cargar()
    }

    fun actualizarOpciones(nuevasOpciones: OpcionesLista) {
        _opciones.value = nuevasOpciones
    }

    fun actualizarConsulta(nuevaConsulta: String) {
        _consulta.value = nuevaConsulta
    }

    fun limpiarFiltros() {
        _opciones.value = OpcionesLista()
        _consulta.value = ""
    }

    fun refrescar() {
        val categoria = _categoriaSeleccionada.value
        viewModelScope.launch {
            _refrescando.value = true
            _aviso.value = null
            val resultado = repository.refrescar(categoria)
            _refrescando.value = false
            resultado.onFailure { error ->
                val guardados = repository.observarPorCategoria(categoria).first()
                if (guardados.isNotEmpty()) {
                    _aviso.value = avisoDatosGuardados(error)
                }
            }
        }
    }

    fun reintentar() = cargar()

    fun cerrarSesion() = auth.cerrarSesion()

    /** Room avisa del cambio y la lista se actualiza sola. */
    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repository.alternarFavorito(id) }
    }

    private fun cargar() {
        trabajo?.cancel() // al cambiar de categoria se descarta la carga y la observacion anteriores
        val categoria = _categoriaSeleccionada.value
        _estado.value = UiState.Cargando
        _aviso.value = null
        trabajo = viewModelScope.launch {
            var termino = false

            // Lo que haya en Room se muestra de inmediato y se actualiza solo cuando cambia.
            launch {
                repository.observarPorCategoria(categoria).collect { items ->
                    if (items.isNotEmpty() || termino) _estado.value = UiState.Exito(items)
                }
            }

            val resultado = repository.refrescar(categoria)
            termino = true
            val guardados = repository.observarPorCategoria(categoria).first()
            _estado.value = resultado.fold(
                onSuccess = { UiState.Exito(guardados) },
                onFailure = {
                    if (guardados.isNotEmpty()) {
                        // Sin red pero con datos guardados: se muestran y se avisa al usuario.
                        _aviso.value = avisoDatosGuardados(it)
                        UiState.Exito(guardados)
                    } else {
                        UiState.Error(mensajeDeError(it))
                    }
                }
            )
        }
    }

    companion object {
        fun factory(
            repository: ItemRepository,
            carritoRepository: CarritoRepository,
            auth: AuthRepository
        ) = viewModelFactory {
            initializer { ListaViewModel(repository, carritoRepository, auth) }
        }
    }
}
