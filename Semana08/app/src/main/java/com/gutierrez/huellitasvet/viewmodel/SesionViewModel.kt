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

    /** Regla de negocio: valida campos, credenciales y que el rol de la cuenta coincida con el rol elegido. */
    fun login(correo: String, clave: String, rolSeleccionado: Rol, onExito: (Rol) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa todos los campos"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name -> _error.value = "Esa cuenta no corresponde a este perfil"
                else -> {
                    prefs.guardarSesion(usuario.rol, usuario.id)
                    _usuarioActual.value = usuario
                    _error.value = null
                    onExito(Rol.valueOf(usuario.rol))
                }
            }
        }
    }

    /** Regla de negocio del registro: valida los campos, crea la cuenta y abre la sesion. */
    fun registrar(
        nombre: String, apellido: String, correo: String, clave: String, confirmar: String,
        codigoAdmin: String, rol: Rol, onExito: (Rol) -> Unit
    ) {
        val nom = nombre.trim()
        val ape = apellido.trim()
        val mail = correo.trim()
        val cod = codigoAdmin.trim()

        when {
            nom.isBlank() || ape.isBlank() || mail.isBlank() || clave.isBlank() || confirmar.isBlank() ->
                _error.value = "Completa todos los campos"
            rol == Rol.ADMINISTRADOR && (cod.isBlank() || cod.length < 4 || cod.contains(" ")) ->
                _error.value = "El código de administrador debe tener al menos 4 caracteres y sin espacios"
            !mail.contains("@") || !mail.contains(".") -> _error.value = "Ingresa un correo válido"
            clave.length < 4 -> _error.value = "La contraseña debe tener al menos 4 caracteres"
            clave != confirmar -> _error.value = "Las contraseñas no coinciden"
            else -> viewModelScope.launch {
                if (repo.existeCorreo(mail) > 0) {
                    _error.value = "Ese correo ya está registrado"
                    return@launch
                }
                if (rol == Rol.ADMINISTRADOR && repo.existeCodigoAdmin(cod) > 0) {
                    _error.value = "Ese código de administrador ya existe"
                    return@launch
                }
                val nuevo = Usuario(
                    nombre = nom,
                    apellido = ape,
                    correo = mail,
                    clave = clave,
                    rol = rol.name,
                    codigoAdmin = if (rol == Rol.ADMINISTRADOR) cod else null
                )
                val id = repo.registrarUsuario(nuevo)
                if (id < 0) {
                    _error.value = "No se pudo registrar el usuario"
                } else {
                    val creado = nuevo.copy(id = id.toInt())
                    prefs.guardarSesion(creado.rol, creado.id)
                    _usuarioActual.value = creado
                    _error.value = null
                    onExito(rol)
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
