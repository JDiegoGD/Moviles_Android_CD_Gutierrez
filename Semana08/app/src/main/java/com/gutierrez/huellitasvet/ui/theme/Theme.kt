package com.gutierrez.huellitasvet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gutierrez.huellitasvet.model.Rol

/** Entrega un ColorScheme segun el perfil (dueño = verde, veterinario = azul, administrador = violeta). */
@Composable
fun VetTheme(rol: Rol, oscuro: Boolean, content: @Composable () -> Unit) {
    val esquema = if (oscuro) {
        when (rol) {
            Rol.CLIENTE -> darkColorScheme(
                primary = Color(0xFF34D399),
                onPrimary = Color(0xFF062016),
                primaryContainer = Color(0xFF064E3B),
                onPrimaryContainer = EsmeraldaContenedor,
                background = Color(0xFF0D1511),
                secondary = Color(0xFF34D399),
                secondaryContainer = Color(0xFF064E3B),
                onSecondaryContainer = EsmeraldaContenedor,
                onBackground = Color(0xFFE6EDEA),
                surface = Color(0xFF141E19),
                onSurface = Color(0xFFE6EDEA),
                surfaceVariant = Color(0xFF1C2A23),
                onSurfaceVariant = Color(0xFFB8C4BE),
                outline = Color(0xFF55635C)
            )
            Rol.ADMIN -> darkColorScheme(
                primary = Color(0xFF93C5FD),
                onPrimary = Color(0xFF0F172A),
                primaryContainer = Color(0xFF1E3A8A),
                onPrimaryContainer = AzulContenedor,
                background = Color(0xFF0D1220),
                secondary = Color(0xFF93C5FD),
                secondaryContainer = Color(0xFF1E3A8A),
                onSecondaryContainer = AzulContenedor,
                onBackground = Color(0xFFE6EDEA),
                surface = Color(0xFF131A2B),
                onSurface = Color(0xFFE6EDEA),
                surfaceVariant = Color(0xFF1B2540),
                onSurfaceVariant = Color(0xFFB8C4BE),
                outline = Color(0xFF55635C)
            )
            Rol.ADMINISTRADOR -> darkColorScheme(
                primary = Color(0xFFA78BFA),
                onPrimary = Color(0xFF1E1B4B),
                primaryContainer = Color(0xFF312E81),
                onPrimaryContainer = Color(0xFFEDE9FE),
                background = Color(0xFF0F0E17),
                secondary = Color(0xFFA78BFA),
                secondaryContainer = Color(0xFF312E81),
                onSecondaryContainer = Color(0xFFEDE9FE),
                onBackground = Color(0xFFE2E8F0),
                surface = Color(0xFF1A1829),
                onSurface = Color(0xFFE2E8F0),
                surfaceVariant = Color(0xFF262338),
                onSurfaceVariant = Color(0xFFCBD5E1),
                outline = Color(0xFF64748B)
            )
        }
    } else {
        when (rol) {
            Rol.CLIENTE -> lightColorScheme(
                primary = EsmeraldaPrimario,
                onPrimary = Color.White,
                primaryContainer = EsmeraldaContenedor,
                onPrimaryContainer = Color(0xFF064E3B),
                background = EsmeraldaFondo,
                secondary = EsmeraldaPrimario,
                secondaryContainer = EsmeraldaContenedor,
                onSecondaryContainer = Color(0xFF064E3B),
                onBackground = Color(0xFF1F2937),
                surface = Color.White,
                onSurface = Color(0xFF1F2937),
                surfaceVariant = Color(0xFFEAF7F0),
                onSurfaceVariant = Color(0xFF6B7280),
                outline = Color(0xFFCBD5E1)
            )
            Rol.ADMIN -> lightColorScheme(
                primary = AzulPrimario,
                onPrimary = Color.White,
                primaryContainer = AzulContenedor,
                onPrimaryContainer = Color(0xFF1E3A8A),
                background = AzulFondo,
                secondary = AzulPrimario,
                secondaryContainer = AzulContenedor,
                onSecondaryContainer = Color(0xFF1E3A8A),
                onBackground = Color(0xFF1F2937),
                surface = Color.White,
                onSurface = Color(0xFF1F2937),
                surfaceVariant = Color(0xFFEEF2FF),
                onSurfaceVariant = Color(0xFF6B7280),
                outline = Color(0xFFCBD5E1)
            )
            Rol.ADMINISTRADOR -> lightColorScheme(
                primary = Color(0xFF7C3AED),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFEDE9FE),
                onPrimaryContainer = Color(0xFF4C1D95),
                background = Color(0xFFF5F3FF),
                secondary = Color(0xFF7C3AED),
                secondaryContainer = Color(0xFFEDE9FE),
                onSecondaryContainer = Color(0xFF4C1D95),
                onBackground = Color(0xFF1F2937),
                surface = Color.White,
                onSurface = Color(0xFF1F2937),
                surfaceVariant = Color(0xFFEDE9FE),
                onSurfaceVariant = Color(0xFF6B7280),
                outline = Color(0xFFCBD5E1)
            )
        }
    }
    MaterialTheme(colorScheme = esquema, typography = VetTypography, shapes = VetShapes, content = content)
}
