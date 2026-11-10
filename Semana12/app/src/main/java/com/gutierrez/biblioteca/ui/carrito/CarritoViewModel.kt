package com.gutierrez.biblioteca.ui.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.repository.CarritoRepository
import com.gutierrez.biblioteca.domain.model.LineaCarrito
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CarritoUiState(
    val lineas: List<LineaCarrito> = emptyList(),
    val totalUnidades: Int = 0,
    val totalDinero: Double = 0.0
)

/** ViewModel del carrito de compras. */
class CarritoViewModel(
    private val repository: CarritoRepository
) : ViewModel() {

    val estado: StateFlow<CarritoUiState> = repository.observarCarrito()
        .map { lineas ->
            CarritoUiState(
                lineas = lineas,
                totalUnidades = lineas.sumOf { it.cantidad },
                totalDinero = lineas.sumOf { it.subtotal }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CarritoUiState()
        )

    val cantidadCarrito: StateFlow<Int> = repository.observarCarrito()
        .map { lineas -> lineas.sumOf { it.cantidad } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    fun aumentar(productoId: Int) {
        viewModelScope.launch { repository.aumentar(productoId) }
    }

    fun disminuir(productoId: Int) {
        viewModelScope.launch { repository.disminuir(productoId) }
    }

    fun eliminar(productoId: Int) {
        viewModelScope.launch { repository.eliminar(productoId) }
    }

    fun vaciar() {
        viewModelScope.launch { repository.vaciar() }
    }

    companion object {
        fun factory(repository: CarritoRepository) = viewModelFactory {
            initializer { CarritoViewModel(repository) }
        }
    }
}
