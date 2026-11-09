package com.gutierrez.biblioteca.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutierrez.biblioteca.data.auth.AuthRepository
import com.gutierrez.biblioteca.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado de la pantalla de login. Cuando sesion deja de ser null, la pantalla navega. */
data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val restaurando: Boolean = true,
    val ingresando: Boolean = false,
    val error: String? = null,
    val sesion: Sesion? = null
)

class LoginViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    init {
        // Si ya habia una sesion guardada, no se pide el ingreso otra vez.
        viewModelScope.launch {
            val sesion = auth.restaurarSesion()
            _estado.update { it.copy(restaurando = false, sesion = sesion) }
        }
    }

    fun onCorreoCambio(correo: String) = _estado.update { it.copy(correo = correo, error = null) }

    fun onContrasenaCambio(contrasena: String) =
        _estado.update { it.copy(contrasena = contrasena, error = null) }

    fun ingresar() {
        val actual = _estado.value
        if (actual.ingresando) return
        val error = when {
            !actual.correo.contains("@") -> "Ingresa un correo válido."
            actual.contrasena.isBlank() -> "Ingresa tu contraseña."
            else -> null
        }
        if (error != null) {
            _estado.update { it.copy(error = error) }
            return
        }
        _estado.update { it.copy(ingresando = true, error = null) }
        viewModelScope.launch {
            auth.ingresar(actual.correo, actual.contrasena).fold(
                onSuccess = { sesion ->
                    _estado.update { it.copy(ingresando = false, contrasena = "", sesion = sesion) }
                },
                onFailure = { fallo ->
                    _estado.update {
                        it.copy(ingresando = false, error = fallo.message ?: "No se pudo iniciar sesión.")
                    }
                }
            )
        }
    }

    companion object {
        fun factory(auth: AuthRepository) = viewModelFactory {
            initializer { LoginViewModel(auth) }
        }
    }
}
