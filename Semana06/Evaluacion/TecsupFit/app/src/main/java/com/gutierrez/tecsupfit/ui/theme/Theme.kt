package com.gutierrez.tecsupfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Esquema de colores fijo de TECSUP Fit (sin colores dinámicos del fondo de pantalla)
private val TecsupFitColorScheme = lightColorScheme(
    primary = Color(0xFF12705A),              // verde: topBar, botones, chips y pestaña activa
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE1F3EC),     // verde claro: pesa, avatar, indicador del bottomBar
    onPrimaryContainer = Color(0xFF0B4A3B),
    secondaryContainer = Color(0xFFE1F3EC),
    onSecondaryContainer = Color(0xFF0B4A3B),
    background = Color.White,
    surface = Color.White,                    // fondo de pantallas y TopAppBar blancas
    surfaceVariant = Color(0xFFF2F2F2),       // fondo gris claro de las tarjetas
    onSurfaceVariant = Color(0xFF6B6B6B),     // textos secundarios
    surfaceContainer = Color.White            // fondo del bottomBar
)

@Composable
fun TecsupFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TecsupFitColorScheme,
        typography = Typography,   // viene de Type.kt (no se modifica)
        content = content
    )
}