package com.gutierrez.biblioteca.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "items" de Room. Guarda los libros descargados para usarlos sin internet
 * y recuerda cuales son favoritos.
 *
 * carrera guarda el nombre del enum Carrera (por ejemplo "DISENO_DESARROLLO_SOFTWARE")
 * para poder listar los libros de cada carrera desde la base de datos.
 */
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val carrera: String,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val descripcion: String = "",
    val precio: Double? = null,
    val valoracion: Double? = null,
    val esFavorito: Boolean = false
)
