package com.gutierrez.biblioteca.data.repository

import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item
import kotlinx.coroutines.flow.Flow

/**
 * Contrato que usan los ViewModels. Ellos no saben si los datos vienen de
 * Retrofit, de Room o de otra fuente: eso lo decide la implementacion.
 *
 * Room es la fuente unica de verdad: la UI observa la base de datos y
 * refrescar() se encarga de actualizarla con lo que responde la API.
 */
interface ItemRepository {

    /** Libros de una carrera guardados en Room; se vuelve a emitir cuando cambian. */
    fun observarPorCarrera(carrera: Carrera): Flow<List<Item>>

    /** Descarga los libros de la carrera y los guarda en Room (conserva los favoritos). */
    suspend fun refrescar(carrera: Carrera): Result<Unit>

    /** Libros marcados como favoritos (de todas las carreras); se vuelve a emitir al cambiar. */
    fun observarFavoritos(): Flow<List<Item>>

    /** Marca o desmarca un libro como favorito en Room. */
    suspend fun alternarFavorito(id: Int)

    /** Detalle de un libro (segundo endpoint); si no hay red usa la copia guardada. */
    suspend fun obtenerDetalle(id: Int): Result<Item>
}
