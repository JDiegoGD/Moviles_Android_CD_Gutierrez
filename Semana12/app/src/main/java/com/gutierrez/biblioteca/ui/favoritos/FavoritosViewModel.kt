package com.gutierrez.biblioteca.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.repository.CarritoRepository
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.mensajeDeError
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Expone los favoritos guardados en Room y el carrito. */
class FavoritosViewModel(
    private val repository: ItemRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    val estado: StateFlow<UiState<List<Item>>> = repository.observarFavoritos()
        .map<List<Item>, UiState<List<Item>>> { UiState.Exito(it) }
        .catch { emit(UiState.Error(mensajeDeError(it))) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    val cantidadCarrito: StateFlow<Int> = carritoRepository.observarCarrito()
        .map { lineas -> lineas.sumOf { it.cantidad } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    fun quitarFavorito(id: Int) {
        viewModelScope.launch { repository.alternarFavorito(id) }
    }

    companion object {
        fun factory(repository: ItemRepository, carritoRepository: CarritoRepository) = viewModelFactory {
            initializer { FavoritosViewModel(repository, carritoRepository) }
        }
    }
}
