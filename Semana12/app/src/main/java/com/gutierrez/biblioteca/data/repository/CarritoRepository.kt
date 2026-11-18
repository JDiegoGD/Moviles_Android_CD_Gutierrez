package com.gutierrez.biblioteca.data.repository

import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.domain.model.LineaCarrito
import kotlinx.coroutines.flow.Flow

/** Contrato del repositorio del carrito de compras. */
interface CarritoRepository {

    fun observarCarrito(): Flow<List<LineaCarrito>>

    suspend fun agregar(item: Item)

    suspend fun aumentar(productoId: Int)

    suspend fun disminuir(productoId: Int)

    suspend fun eliminar(productoId: Int)

    suspend fun vaciar()
}
