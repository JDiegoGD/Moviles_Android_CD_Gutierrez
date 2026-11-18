package com.gutierrez.biblioteca.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/** DAO para la tabla "carrito". */
@Dao
interface CarritoDao {

    @Query("SELECT * FROM carrito ORDER BY titulo ASC")
    fun observar(): Flow<List<CarritoEntity>>

    @Query("SELECT * FROM carrito WHERE productoId = :productoId")
    suspend fun obtenerPorId(productoId: Int): CarritoEntity?

    @Upsert
    suspend fun guardar(entity: CarritoEntity)

    @Query("UPDATE carrito SET cantidad = cantidad + 1 WHERE productoId = :productoId")
    suspend fun sumarUno(productoId: Int)

    @Query("UPDATE carrito SET cantidad = :cantidad WHERE productoId = :productoId")
    suspend fun actualizarCantidad(productoId: Int, cantidad: Int)

    @Query("DELETE FROM carrito WHERE productoId = :productoId")
    suspend fun eliminar(productoId: Int)

    @Query("DELETE FROM carrito")
    suspend fun vaciar()
}
