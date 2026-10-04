package com.gutierrez.citas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = AzulClinica,
    onPrimary = Color.White,
    secondary = AzulNoche,
    tertiary = AzulNiebla,
    background = FondoApp,
    surface = Color.White,
    error = RojoAlerta
)

@Composable
fun SaludPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}