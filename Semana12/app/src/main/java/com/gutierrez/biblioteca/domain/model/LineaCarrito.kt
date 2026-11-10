package com.gutierrez.biblioteca.domain.model

/** Modelo de dominio para un producto en el carrito de compras. */
data class LineaCarrito(
    val productoId: Int,
    val titulo: String,
    val imagenUrl: String?,
    val precio: Double,
    val cantidad: Int
) {
    val subtotal: Double get() = precio * cantidad
}
