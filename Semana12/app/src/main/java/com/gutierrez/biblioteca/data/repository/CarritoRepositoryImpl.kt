package com.gutierrez.biblioteca.data.repository

import com.gutierrez.biblioteca.data.local.CarritoDao
import com.gutierrez.biblioteca.data.local.CarritoEntity
import com.gutierrez.biblioteca.domain.model.Item
import com.gutierrez.biblioteca.domain.model.LineaCarrito
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Implementacion de CarritoRepository usando Room (CarritoDao). */
class CarritoRepositoryImpl(
    private val dao: CarritoDao
) : CarritoRepository {

    override fun observarCarrito(): Flow<List<LineaCarrito>> =
        dao.observar().map { entidades -> entidades.map { it.toLineaCarrito() } }

    override suspend fun agregar(item: Item) {
        val existente = dao.obtenerPorId(item.id)
        if (existente != null) {
            dao.sumarUno(item.id)
        } else {
            dao.guardar(
                CarritoEntity(
                    productoId = item.id,
                    titulo = item.titulo,
                    imagenUrl = item.imagenUrl,
                    precio = item.precio ?: 0.0,
                    cantidad = 1
                )
            )
        }
    }

    override suspend fun aumentar(productoId: Int) {
        dao.sumarUno(productoId)
    }

    override suspend fun disminuir(productoId: Int) {
        val existente = dao.obtenerPorId(productoId) ?: return
        if (existente.cantidad <= 1) {
            dao.eliminar(productoId)
        } else {
            dao.actualizarCantidad(productoId, existente.cantidad - 1)
        }
    }

    override suspend fun eliminar(productoId: Int) {
        dao.eliminar(productoId)
    }

    override suspend fun vaciar() {
        dao.vaciar()
    }
}

private fun CarritoEntity.toLineaCarrito(): LineaCarrito = LineaCarrito(
    productoId = productoId,
    titulo = titulo,
    imagenUrl = imagenUrl,
    precio = precio,
    cantidad = cantidad
)
