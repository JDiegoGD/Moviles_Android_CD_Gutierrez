package com.gutierrez.biblioteca.data.repository

import com.gutierrez.biblioteca.domain.model.Categoria
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

    /** Productos de una categoria guardados en Room; se vuelve a emitir cuando cambian. */
    fun observarPorCategoria(categoria: Categoria): Flow<List<Item>>

    /** Descarga los productos de la categoria y los guarda en Room (conserva los favoritos). */
    suspend fun refrescar(categoria: Categoria): Result<Unit>

    /** Productos marcados como favoritos (de todas las categorías); se vuelve a emitir al cambiar. */
    fun observarFavoritos(): Flow<List<Item>>

    /** Marca o desmarca un producto como favorito en Room. */
    suspend fun alternarFavorito(id: Int)

    /** Total de productos guardados en Room (panel Admin). */
    suspend fun contarProductos(): Int

    /** Total de favoritos guardados en Room (panel Admin). */
    suspend fun contarFavoritos(): Int

    /** Refresca todas las categorías; falla si alguna no se pudo actualizar (panel Admin). */
    suspend fun sincronizarTodo(): Result<Unit>

    /** Borra los productos guardados salvo los favoritos (panel Admin). */
    suspend fun limpiarCache()

    /** Detalle de un producto (segundo endpoint); si no hay red usa la copia guardada. */
    suspend fun obtenerDetalle(id: Int): Result<Item>
}
