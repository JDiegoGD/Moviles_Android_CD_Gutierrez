package com.gutierrez.biblioteca.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/** Consultas de Room. Las que devuelven Flow se vuelven a emitir cuando cambia la tabla. */
@Dao
interface ItemDao {

    @Query("SELECT * FROM items WHERE carrera = :carrera ORDER BY titulo")
    fun porCarrera(carrera: String): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE esFavorito = 1 ORDER BY titulo")
    fun favoritos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun porId(id: Int): ItemEntity?

    /** Ids de favoritos: permite conservarlos al reemplazar los datos descargados. */
    @Query("SELECT id FROM items WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<Int>

    @Query("SELECT COUNT(*) FROM items")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM items WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    /** Inserta los nuevos y actualiza los que ya existen (mismo id). */
    @Upsert
    suspend fun guardarTodos(items: List<ItemEntity>)

    @Query("UPDATE items SET esFavorito = :favorito WHERE id = :id")
    suspend fun marcarFavorito(id: Int, favorito: Boolean)

    @Query("DELETE FROM items")
    suspend fun borrarTodos()
}
