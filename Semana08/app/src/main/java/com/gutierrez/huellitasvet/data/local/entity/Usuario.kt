package com.gutierrez.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apellido: String = "",
    val correo: String,
    val clave: String,
    val rol: String,            // Rol.name: CLIENTE | ADMIN | ADMINISTRADOR
    val codigoAdmin: String? = null
)

val Usuario.nombreCompleto: String
    get() = if (apellido.isBlank()) nombre else "$nombre $apellido"
