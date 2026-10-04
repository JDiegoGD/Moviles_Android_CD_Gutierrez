package com.gutierrez.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 8 · Iniciar sesión (no viene en el diseño: hay que dibujarla con el mismo estilo)
// TODO: P8-1  Campos de correo y contraseña (la contraseña oculta) guardados en estados.
// TODO: P8-2  Al pulsar "Iniciar sesión" llamar a Repositorio.iniciarSesion().
//             Si devuelve true -> onLoginExitoso(); si no, mostrar un error de credenciales.
// TODO: P8-3  Enlace "Crear cuenta" -> onIrRegistro()   |   flecha de volver -> onAtras()
// TODO: P8-4  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrRegistro: () -> Unit,
    onAtras: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Iniciar sesión",
        acciones = listOf(
            "Simular inicio de sesión" to onLoginExitoso,
            "Crear cuenta" to onIrRegistro,
            "Atrás" to onAtras
        )
    )
}
