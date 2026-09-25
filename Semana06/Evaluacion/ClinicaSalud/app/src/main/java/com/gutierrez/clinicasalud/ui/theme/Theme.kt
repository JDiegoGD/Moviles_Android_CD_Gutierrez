package com.gutierrez.clinicasalud.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Esquema de colores fijo de Clínica Salud+ (sin colores dinámicos del fondo de pantalla)
private val ClinicaColorScheme = lightColorScheme(
    primary = Color(0xFF5B2C8F),              // morado: topBar, botones, chips seleccionados
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE4F7),     // lila claro: avatares
    onPrimaryContainer = Color(0xFF3A1464),
    secondaryContainer = Color(0xFFEDE4F7),   // lila claro: opción activa del menú
    onSecondaryContainer = Color(0xFF3A1464),
    background = Color.White,
    surface = Color.White,
    surfaceVariant = Color(0xFFF6F2FA),       // fondo de las tarjetas
    onSurfaceVariant = Color(0xFF6B6B6B),     // textos secundarios
    surfaceContainerLow = Color.White         // fondo del menú lateral
)

@Composable
fun ClinicaSaludTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ClinicaColorScheme,
        typography = Typography,
        content = content
    )
}