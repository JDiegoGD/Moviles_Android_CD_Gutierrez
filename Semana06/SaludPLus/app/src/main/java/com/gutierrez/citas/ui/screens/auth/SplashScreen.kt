package com.gutierrez.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.gutierrez.citas.ui.components.PantallaEnConstruccion

// Pantalla 1 · Splash (bienvenida)
// TODO: P1-1  Mostrar el logo y la imagen del médico con Image (hay que agregar los dibujos a res/drawable).
// TODO: P1-2  Acomodar todo en un Column: título, frase de la clínica y los dos botones.
// TODO: P1-3  "Comenzar" -> onComenzar()   |   "Ya tengo cuenta" -> onYaTengoCuenta()
// TODO: P1-4  Borrar la llamada a PantallaEnConstruccion.
@Composable
fun SplashScreen(
    onComenzar: () -> Unit,
    onYaTengoCuenta: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Splash",
        acciones = listOf(
            "Comenzar" to onComenzar,
            "Ya tengo cuenta" to onYaTengoCuenta
        )
    )
}
