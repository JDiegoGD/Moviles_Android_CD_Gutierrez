package com.gutierrez.biblioteca.ui.lista

import androidx.lifecycle.ViewModel
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel de la lista. Guarda el estado y decide que hacer con cada evento;
 * la pantalla solo lo observa.
 *
 * PROVISIONAL (H4): usa datos de prueba. En H6 se conecta al repositorio y a la API.
 */
class ListaViewModel : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Exito(itemsDePrueba))
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    fun reintentar() {
        _estado.value = UiState.Exito(itemsDePrueba)
    }
}

/** Datos de prueba para H4. Se eliminan cuando la lista consuma la API (H6). */
internal val itemsDePrueba = listOf(
    Item(1, "Cien años de soledad", "Gabriel García Márquez", null),
    Item(2, "Don Quijote de la Mancha", "Miguel de Cervantes", null),
    Item(3, "La ciudad y los perros", "Mario Vargas Llosa", null),
    Item(4, "Rayuela", "Julio Cortázar", null),
    Item(5, "El principito", "Antoine de Saint-Exupéry", null),
    Item(6, "Pedro Páramo", "Juan Rulfo", null),
    Item(7, "Ficciones", "Jorge Luis Borges", null),
    Item(8, "Los ríos profundos", "José María Arguedas", null)
)
