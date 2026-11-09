package com.gutierrez.biblioteca.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "items" de Room. Guarda los productos descargados para usarlos sin internet
 * y recuerda cuales son favoritos.
 *
 * categoria guarda el nombre del enum Categoria (por ejemplo "LAPTOPS")
 * para poder listar los productos de cada categoria desde la base de datos.
 */
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val categoria: String,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val descripcion: String = "",
    val precio: Double? = null,
    val valoracion: Double? = null,
    val esFavorito: Boolean = false
)
