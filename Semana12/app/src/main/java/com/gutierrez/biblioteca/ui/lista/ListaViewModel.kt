package com.gutierrez.biblioteca.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.ui.UiState
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la lista. Guarda el estado y decide que hacer con cada evento
 * (elegir carrera, reintentar); la pantalla solo lo observa.
 */
class ListaViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _carreraSeleccionada = MutableStateFlow(Carrera.entries.first())
    val carreraSeleccionada: StateFlow<Carrera> = _carreraSeleccionada.asStateFlow()

    private var trabajo: Job? = null

    init {
        cargar()
    }

    fun seleccionar(carrera: Carrera) {
        if (carrera == _carreraSeleccionada.value) return
        _carreraSeleccionada.value = carrera
        cargar()
    }

    fun reintentar() = cargar()

    private fun cargar() {
        trabajo?.cancel() // si el usuario cambia de carrera, se descarta la carga anterior
        _estado.value = UiState.Cargando
        trabajo = viewModelScope.launch {
            _estado.value = repository.listarPorCarrera(_carreraSeleccionada.value).fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(mensajeDe(it)) }
            )
        }
    }

    private fun mensajeDe(error: Throwable): String =
        if (error is IOException) {
            "No se pudo conectar. Revisa tu conexión a internet."
        } else {
            "Ocurrió un error al cargar los libros."
        }

    companion object {
        fun factory(repository: ItemRepository) = viewModelFactory {
            initializer { ListaViewModel(repository) }
        }
    }
}
