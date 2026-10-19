package com.gutierrez.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.data.prefs.PreferenciasStore
import com.gutierrez.huellitasvet.data.repository.VetRepository
import com.gutierrez.huellitasvet.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Sesion de la app: quien entro, errores de login y preferencias (DataStore). */
class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        // Si habia un perfil guardado, recupera su usuario (el Splash salta el login)
        viewModelScope.launch {
            val id = prefs.usuarioId.first()
            if (id > 0) _usuarioActual.value = repo.obtenerUsuario(id)
        }
    }

    /** Regla de negocio: valida campos, credenciales y que el rol coincida con la pestaña. */
    fun login(correo: String, clave: String, rolSeleccionado: Rol, onExito: (Rol) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa todos los campos"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name -> _error.value =
                    if (rolSeleccionado == Rol.CLIENTE) "Esta cuenta no es de dueño"
                    else "Esta cuenta no es de veterinario"
                else -> {
                    prefs.guardarSesion(usuario.rol, usuario.id)
                    _usuarioActual.value = usuario
                    _error.value = null
                    onExito(Rol.valueOf(usuario.rol))
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    /** Borra la sesion guardada (el modo oscuro se conserva) y avisa para volver al login. */
    fun cerrarSesion(onListo: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
            _error.value = null
            onListo()
        }
    }

    /**
     * Modo practica: guarda el OTRO perfil (dueño <-> veterinario) y avisa para
     * navegar a su grafo. Usa las cuentas de prueba de los datos iniciales.
     */
    fun cambiarPerfil(onListo: (Rol) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val (correo, clave) =
            if (actual.rol == Rol.CLIENTE.name) "vet@huellitas.test" to "admin"
            else "ana@correo.test" to "1234"
        viewModelScope.launch {
            val otro = repo.login(correo, clave)
            if (otro == null) {
                _error.value = "No se encontró el otro perfil"
                return@launch
            }
            prefs.guardarSesion(otro.rol, otro.id)
            _usuarioActual.value = otro
            onListo(Rol.valueOf(otro.rol))
        }
    }

    // ---------- Preferencias (DataStore) ----------
    fun cambiarModoOscuro(valor: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(valor) }
    }

    fun cambiarNotificaciones(valor: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(valor) }
    }

    fun cambiarAceptarCitas(valor: Boolean) {
        viewModelScope.launch { prefs.guardarAceptarCitas(valor) }
    }
}
