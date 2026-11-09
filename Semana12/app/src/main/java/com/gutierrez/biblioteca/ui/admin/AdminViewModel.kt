package com.gutierrez.biblioteca.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.auth.AuthRepository
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.domain.model.Rol
import com.gutierrez.biblioteca.notification.Notificador
import com.gutierrez.biblioteca.ui.mensajeDeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado del panel Admin. permitido es false si quien llego aqui no tiene el rol ADMIN. */
data class AdminUiState(
    val permitido: Boolean,
    val totalProductos: Int = 0,
    val totalFavoritos: Int = 0,
    val ocupado: Boolean = false,
    val mensaje: String? = null
)

class AdminViewModel(
    private val repository: ItemRepository,
    auth: AuthRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow(
        AdminUiState(permitido = auth.sesionActual()?.rol == Rol.ADMIN)
    )
    val estado: StateFlow<AdminUiState> = _estado.asStateFlow()

    init {
        if (_estado.value.permitido) {
            viewModelScope.launch { leerTotales() }
        }
    }

    fun sincronizar() = ejecutar {
        val mensaje = repository.sincronizarTodo().fold(
            onSuccess = { "Sincronización completa de todas las categorías." },
            onFailure = { "No se pudo sincronizar todo. " + mensajeDeError(it) }
        )
        notificador.mostrar("Catálogo de Productos", mensaje)
        mensaje
    }

    fun limpiarCache() = ejecutar {
        repository.limpiarCache()
        "Caché limpiada. Los favoritos se conservaron."
    }

    /** Corre una accion del panel (solo si es admin y no hay otra en curso) y refresca los totales. */
    private fun ejecutar(accion: suspend () -> String) {
        val actual = _estado.value
        if (!actual.permitido || actual.ocupado) return
        _estado.update { it.copy(ocupado = true, mensaje = null) }
        viewModelScope.launch {
            val mensaje = accion()
            leerTotales()
            _estado.update { it.copy(ocupado = false, mensaje = mensaje) }
        }
    }

    private suspend fun leerTotales() {
        val productos = repository.contarProductos()
        val favoritos = repository.contarFavoritos()
        _estado.update { it.copy(totalProductos = productos, totalFavoritos = favoritos) }
    }

    companion object {
        fun factory(
            repository: ItemRepository,
            auth: AuthRepository,
            notificador: Notificador
        ) = viewModelFactory {
            initializer { AdminViewModel(repository, auth, notificador) }
        }
    }
}
