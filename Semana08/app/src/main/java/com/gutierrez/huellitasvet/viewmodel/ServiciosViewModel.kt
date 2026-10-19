package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Servicio
import com.gutierrez.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Errores por campo: "nombre", "precio"
    private val _errores = MutableStateFlow<Map<String, String>>(emptyMap())
    val errores: StateFlow<Map<String, String>> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    /** Guarda un servicio nuevo (id = 0) o editado. Nombre obligatorio y precio > 0. */
    fun guardar(s: Servicio, onGuardado: () -> Unit) {
        val errores = mutableMapOf<String, String>()
        if (s.nombre.isBlank()) errores["nombre"] = "El nombre es obligatorio"
        if (s.precio <= 0.0) errores["precio"] = "Debe ser mayor que 0"
        _errores.value = errores
        if (errores.isNotEmpty()) return
        viewModelScope.launch {
            repo.guardarServicio(s.copy(nombre = s.nombre.trim(), descripcion = s.descripcion.trim()))
            onGuardado()
        }
    }

    /** Regla: un servicio con citas no se elimina (solo se desactiva). */
    fun eliminar(s: Servicio, onEliminado: () -> Unit) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(s.id)) {
                _mensaje.value = "No se puede eliminar \"${s.nombre}\": tiene citas. Puedes desactivarlo."
            } else {
                repo.eliminarServicio(s)
                onEliminado()
            }
        }
    }

    fun limpiarErrores() { _errores.value = emptyMap() }

    fun limpiarMensaje() { _mensaje.value = null }
}
