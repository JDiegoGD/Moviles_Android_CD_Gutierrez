package com.gutierrez.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Horario de atencion de UN dia de la semana.
 * diaSemana: 1 = lunes ... 7 = domingo (igual que DayOfWeek.value de Java).
 * Las horas se guardan como texto "HH:mm".
 * Si descansoInicio == descansoFin significa que ese dia no hay descanso.
 */
@Entity(tableName = "horario_atencion")
data class HorarioAtencion(
    @PrimaryKey val diaSemana: Int,
    val abierto: Boolean,
    val apertura: String,
    val cierre: String,
    val descansoInicio: String,
    val descansoFin: String
)
