package com.gutierrez.campusgps.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = CyanPrimary,
    onPrimary = Color.White,
    secondary = NavyBlue,
    onSecondary = Color.White,
    background = BackgroundWhite,
    surface = SurfaceWhite,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun CampusGPSTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
