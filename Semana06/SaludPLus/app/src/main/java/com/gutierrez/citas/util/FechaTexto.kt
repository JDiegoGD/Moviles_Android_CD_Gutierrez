package com.gutierrez.citas.util

private val nombresMes = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "setiembre", "octubre", "noviembre", "diciembre"
)

/**
 * Las citas guardan la fecha como "2026-10-12"; para mostrarla la paso a "12 de octubre de 2026".
 * Si el texto no tiene ese formato lo devuelvo tal cual.
 */
fun fechaLegible(fechaIso: String): String {
    val partes = fechaIso.split("-")
    if (partes.size != 3) return fechaIso
    val dia = partes[2].toIntOrNull() ?: return fechaIso
    val mes = partes[1].toIntOrNull() ?: return fechaIso
    if (mes !in 1..12) return fechaIso
    return "$dia de ${nombresMes[mes - 1]} de ${partes[0]}"
}

/** "13:00" -> "13:00 a 13:30": cada cita dura media hora. */
fun rangoDeHora(inicio: String): String {
    val partes = inicio.split(":")
    val h = partes.getOrNull(0)?.toIntOrNull() ?: return inicio
    val m = partes.getOrNull(1)?.toIntOrNull() ?: return inicio
    val total = h * 60 + m + 30
    val fin = "%02d:%02d".format((total / 60) % 24, total % 60)
    return "$inicio a $fin"
}