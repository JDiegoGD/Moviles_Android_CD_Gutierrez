package com.gutierrez.biblioteca.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.mensajeDeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Carga el detalle de un libro (segundo endpoint) y expone su estado. */
class DetalleViewModel(
    private val repository: ItemRepository,
    private val id: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun reintentar() = cargar()

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
        fun factory(repository: ItemRepository, id: Int) = viewModelFactory {
            initializer { DetalleViewModel(repository, id) }
        }
    }
}
