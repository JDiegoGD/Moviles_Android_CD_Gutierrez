package com.gutierrez.biblioteca.ui.registro

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

/** Estado de la pantalla de registro. Cuando sesion deja de ser null, la pantalla navega. */
data class RegistroUiState(
    val correo: String = "",
    val contrasena: String = "",
    val confirmacion: String = "",
    val registrando: Boolean = false,
    val error: String? = null,
    val sesion: Sesion? = null
)

class RegistroViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(RegistroUiState())
    val estado: StateFlow<RegistroUiState> = _estado.asStateFlow()

    fun onCorreoCambio(correo: String) = _estado.update { it.copy(correo = correo, error = null) }

    fun onContrasenaCambio(contrasena: String) =
        _estado.update { it.copy(contrasena = contrasena, error = null) }

    fun onConfirmacionCambio(confirmacion: String) =
        _estado.update { it.copy(confirmacion = confirmacion, error = null) }

    fun registrar() {
        val actual = _estado.value
        if (actual.registrando) return
        val error = when {
            !correoValido(actual.correo) -> "Ingresa un correo válido."
            actual.contrasena.length < MINIMO_CONTRASENA ->
                "La contraseña debe tener al menos $MINIMO_CONTRASENA caracteres."
            actual.contrasena != actual.confirmacion -> "Las contraseñas no coinciden."
            else -> null
        }
        if (error != null) {
            _estado.update { it.copy(error = error) }
            return
        }
        _estado.update { it.copy(registrando = true, error = null) }
        viewModelScope.launch {
            auth.registrar(actual.correo, actual.contrasena).fold(
                onSuccess = { sesion ->
                    _estado.update {
                        it.copy(registrando = false, contrasena = "", confirmacion = "", sesion = sesion)
                    }
                },
                onFailure = { fallo ->
                    _estado.update {
                        it.copy(registrando = false, error = fallo.message ?: "No se pudo crear la cuenta.")
                    }
                }
            )
        }
    }

    private fun correoValido(correo: String): Boolean {
        val texto = correo.trim()
        val arroba = texto.indexOf('@')
        return arroba > 0 && texto.indexOf('.', arroba) > arroba + 1 && !texto.endsWith(".")
    }

    companion object {
        private const val MINIMO_CONTRASENA = 6

        fun factory(auth: AuthRepository) = viewModelFactory {
            initializer { RegistroViewModel(auth) }
        }
    }
}
