package com.gutierrez.biblioteca.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.repository.CarritoRepository
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.mensajeDeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Carga el detalle de un producto y expone su estado y el carrito. */
class DetalleViewModel(
    private val repository: ItemRepository,
    private val carritoRepository: CarritoRepository,
    private val id: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    val cantidadCarrito: StateFlow<Int> = carritoRepository.observarCarrito()
        .map { lineas -> lineas.sumOf { it.cantidad } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    init {
        cargar()
    }

    fun reintentar() = cargar()

    fun alternarFavorito() {
        val actual = (_estado.value as? UiState.Exito)?.datos ?: return
        viewModelScope.launch {
            repository.alternarFavorito(actual.id)
            _estado.value = UiState.Exito(actual.copy(esFavorito = !actual.esFavorito))
        }
    }

    fun agregarAlCarrito() {
        val actual = (_estado.value as? UiState.Exito)?.datos ?: return
        viewModelScope.launch {
            carritoRepository.agregar(actual)
        }
    }

    private fun cargar() {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            _estado.value = repository.obtenerDetalle(id).fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(mensajeDeError(it)) }
            )
        }
    }

    companion object {
        fun factory(repository: ItemRepository, carritoRepository: CarritoRepository, id: Int) = viewModelFactory {
            initializer { DetalleViewModel(repository, carritoRepository, id) }
        }
    }
}
