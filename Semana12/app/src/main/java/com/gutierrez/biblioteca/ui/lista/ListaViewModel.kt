package com.gutierrez.biblioteca.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.auth.AuthRepository
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.domain.model.Rol
import com.gutierrez.biblioteca.ui.UiState
import com.gutierrez.biblioteca.ui.avisoDatosGuardados
import com.gutierrez.biblioteca.ui.mensajeDeError
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel de la lista. Observa Room (fuente unica de verdad) y pide al
 * repositorio que la actualice desde la API; la pantalla solo dibuja el estado.
 */
class ListaViewModel(
    private val repository: ItemRepository,
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _carreraSeleccionada = MutableStateFlow(Carrera.entries.first())
    val carreraSeleccionada: StateFlow<Carrera> = _carreraSeleccionada.asStateFlow()

    /** Aviso para el usuario (por ejemplo, sin conexion); null cuando no hay nada que avisar. */
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    /** El acceso al panel Admin solo se muestra a quien ingreso con el rol ADMIN. */
    val esAdmin: Boolean = auth.sesionActual()?.rol == Rol.ADMIN

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

    fun cerrarSesion() = auth.cerrarSesion()

    /** Room avisa del cambio y la lista se actualiza sola. */
    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repository.alternarFavorito(id) }
    }

    private fun cargar() {
        trabajo?.cancel() // al cambiar de carrera se descarta la carga y la observacion anteriores
        val carrera = _carreraSeleccionada.value
        _estado.value = UiState.Cargando
        _aviso.value = null
        trabajo = viewModelScope.launch {
            var termino = false

            // Lo que haya en Room se muestra de inmediato y se actualiza solo cuando cambia.
            launch {
                repository.observarPorCarrera(carrera).collect { items ->
                    if (items.isNotEmpty() || termino) _estado.value = UiState.Exito(items)
                }
            }

            val resultado = repository.refrescar(carrera)
            termino = true
            val guardados = repository.observarPorCarrera(carrera).first()
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
        fun factory(repository: ItemRepository, auth: AuthRepository) = viewModelFactory {
            initializer { ListaViewModel(repository, auth) }
        }
    }
}
