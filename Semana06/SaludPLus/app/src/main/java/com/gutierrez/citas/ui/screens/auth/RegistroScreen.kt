package com.gutierrez.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 2 · Registro
// TODO: P2-1  Guardar nombre, teléfono, correo y contraseña en estados (remember + mutableStateOf).
// TODO: P2-2  Un OutlinedTextField por dato, con su mensaje de error debajo cuando no cumpla.
// TODO: P2-3  Validar al pulsar "Registrarme": nombre no vacío, teléfono de 9 dígitos,
//             correo con formato válido y contraseña de al menos 6 caracteres.
// TODO: P2-4  Si todo es válido, armar el Usuario y llamar a Repositorio.registrarUsuario().
//             Si devuelve false, mostrar "Este correo ya está registrado".
// TODO: P2-5  Registro correcto -> onRegistroExitoso()   |   "Ya tengo cuenta" -> onIrLogin()
// TODO: P2-6  El enlace de términos y condiciones llama a onTerminos().
// TODO: P2-7  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onIrLogin: () -> Unit,
    onTerminos: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Registro",
        acciones = listOf(
            "Simular registro exitoso" to onRegistroExitoso,
            "Ya tengo cuenta" to onIrLogin,
            "Ver términos y condiciones" to onTerminos
        )
    )
}
