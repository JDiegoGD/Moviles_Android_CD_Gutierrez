package com.gutierrez.huellitasvet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gutierrez.huellitasvet.model.Rol

/** Entrega un ColorScheme segun el perfil (dueño = verde, veterinario = azul). */
@Composable
fun VetTheme(rol: Rol, oscuro: Boolean, content: @Composable () -> Unit) {
    val dueno = rol == Rol.CLIENTE
    val esquema = if (oscuro) {
        darkColorScheme(
            primary = if (dueno) Color(0xFF34D399) else Color(0xFF93C5FD),
            onPrimary = Color(0xFF062016),
            primaryContainer = if (dueno) Color(0xFF064E3B) else Color(0xFF1E3A8A),
            onPrimaryContainer = if (dueno) EsmeraldaContenedor else AzulContenedor,
            background = if (dueno) Color(0xFF0D1511) else Color(0xFF0D1220),
            secondary = if (dueno) Color(0xFF34D399) else Color(0xFF93C5FD),
            secondaryContainer = if (dueno) Color(0xFF064E3B) else Color(0xFF1E3A8A),
            onSecondaryContainer = if (dueno) EsmeraldaContenedor else AzulContenedor,
            onBackground = Color(0xFFE6EDEA),
            surface = if (dueno) Color(0xFF141E19) else Color(0xFF131A2B),
            onSurface = Color(0xFFE6EDEA),
            surfaceVariant = if (dueno) Color(0xFF1C2A23) else Color(0xFF1B2540),
            onSurfaceVariant = Color(0xFFB8C4BE),
            outline = Color(0xFF55635C)
        )
    } else {
        lightColorScheme(
            primary = if (dueno) EsmeraldaPrimario else AzulPrimario,
            onPrimary = Color.White,
            primaryContainer = if (dueno) EsmeraldaContenedor else AzulContenedor,
            onPrimaryContainer = if (dueno) Color(0xFF064E3B) else Color(0xFF1E3A8A),
            background = if (dueno) EsmeraldaFondo else AzulFondo,
            secondary = if (dueno) EsmeraldaPrimario else AzulPrimario,
            secondaryContainer = if (dueno) EsmeraldaContenedor else AzulContenedor,
            onSecondaryContainer = if (dueno) Color(0xFF064E3B) else Color(0xFF1E3A8A),
            onBackground = Color(0xFF1F2937),
            surface = Color.White,
            onSurface = Color(0xFF1F2937),
            surfaceVariant = if (dueno) Color(0xFFEAF7F0) else Color(0xFFEEF2FF),
            onSurfaceVariant = Color(0xFF6B7280),
            outline = Color(0xFFCBD5E1)
        )
    }
    MaterialTheme(colorScheme = esquema, typography = VetTypography, shapes = VetShapes, content = content)
}
