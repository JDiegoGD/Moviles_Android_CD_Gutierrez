package com.gutierrez.citas.data.model

import java.time.DayOfWeek

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val calificacion: Double,
    val aniosExperiencia: Int,
    val resenas: Int,
    val diasAtencion: Set<DayOfWeek>,
    val horaInicio: String,
    val horaFin: String,
    val foto: String
)
