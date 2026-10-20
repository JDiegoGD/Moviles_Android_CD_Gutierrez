package com.gutierrez.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "citas",
    foreignKeys = [
        ForeignKey(
            entity = Mascota::class,
            parentColumns = ["id"],
            childColumns = ["mascotaId"]
        ),
        ForeignKey(
            entity = Servicio::class,
            parentColumns = ["id"],
            childColumns = ["servicioId"]
        )
    ],
    // Un indice por cada FK + (fecha, hora) para acelerar la consulta de horarios ocupados
    indices = [Index("mascotaId"), Index("servicioId"), Index("fecha", "hora")]
)
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mascotaId: Int,
    val servicioId: Int,
    val fecha: String,                  // 'aaaa-MM-dd'
    val hora: String,                   // 'HH:mm'
    val nota: String = "",
    val estado: String,                 // EstadoCita.name
    val precio: Double,                 // "foto" del precio del servicio al pedir la cita
    val diagnostico: String? = null,
    val tratamiento: String? = null,
    val pesoRegistrado: Double? = null,
    val fechaControl: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)
