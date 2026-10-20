package com.gutierrez.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vacunas",
    foreignKeys = [
        ForeignKey(
            entity = Mascota::class,
            parentColumns = ["id"],
            childColumns = ["mascotaId"]
        )
    ],
    indices = [Index("mascotaId")]
)
data class Vacuna(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mascotaId: Int,
    val nombre: String,
    val fecha: String,               // 'aaaa-MM-dd'
    val proximaDosis: String? = null // 'aaaa-MM-dd' o null
)
