package com.gutierrez.biblioteca.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla "carrito" de Room para los productos agregados al carrito de compras. */
@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey val productoId: Int,
    val titulo: String,
    val imagenUrl: String?,
    val precio: Double,
    val cantidad: Int
)
