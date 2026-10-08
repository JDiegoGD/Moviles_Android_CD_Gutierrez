package com.gutierrez.citas.data.model

/**
 * Resultado de un examen médico. [estado] es "Disponible" o "En proceso" y
 * [fecha] va como "2026-09-18".
 */
data class Resultado(
    val id: Int,
    val examen: String,
    val fecha: String,
    val especialidadId: Int,
    val estado: String
)
