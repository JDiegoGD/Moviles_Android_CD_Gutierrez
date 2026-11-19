package com.gutierrez.campusgps.util

import java.text.Normalizer
import java.util.Locale
import kotlin.math.roundToInt

fun formatearDistancia(metros: Float): String {
    return if (metros < 1_000f) {
        "${metros.roundToInt()} m"
    } else {
        String.format(Locale.US, "%.1f km", metros / 1_000f)
    }
}

fun normalizarTexto(texto: String): String {
    val normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(normalizado, "")
        .lowercase(Locale.ROOT)
        .trim()
}