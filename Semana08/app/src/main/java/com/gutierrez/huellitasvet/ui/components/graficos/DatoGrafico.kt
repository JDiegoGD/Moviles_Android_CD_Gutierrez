package com.gutierrez.huellitasvet.ui.components.graficos

import androidx.compose.ui.graphics.Color

/** Dato generico que reciben los graficos del laboratorio de Canvas. */
data class DatoGrafico(
    val etiqueta: String,
    val valor: Float,
    val color: Color = Color.Unspecified
)
