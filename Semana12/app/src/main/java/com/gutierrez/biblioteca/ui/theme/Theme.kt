package com.gutierrez.biblioteca.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val EsquemaClaro = lightColorScheme(
    primary = Naranja,
    onPrimary = Color.White,
    primaryContainer = NaranjaSuave,
    onPrimaryContainer = NaranjaOscuro,
    secondary = TextoSecundario,
    onSecondary = Color.White,
    secondaryContainer = GrisCampo,
    onSecondaryContainer = TextoPrincipal,
    tertiary = EstrellaAmarilla,
    tertiaryContainer = AvisoFondo,
    onTertiaryContainer = AvisoTexto,
    background = FondoClaro,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    surfaceVariant = GrisCampo,
    onSurfaceVariant = TextoSecundario,
    outline = GrisBorde,
    outlineVariant = GrisBorde
)

private val Formas = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

/**
 * Tema del catalogo: siempre claro (blanco y naranja coral), sin seguir el modo
 * oscuro del telefono, para que la app se vea luminosa como las apps de delivery.
 */
@Composable
fun BibliotecaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
