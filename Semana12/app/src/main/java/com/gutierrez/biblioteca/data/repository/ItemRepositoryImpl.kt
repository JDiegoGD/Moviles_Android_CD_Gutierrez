package com.gutierrez.biblioteca.data.repository

import com.gutierrez.biblioteca.data.mapper.toItem
import com.gutierrez.biblioteca.data.remote.ApiService
import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item
import kotlin.coroutines.cancellation.CancellationException

/** H6-H7: los datos salen directo de la API. En H9 Room pasa a ser la fuente de verdad. */
class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    override suspend fun listarPorCarrera(carrera: Carrera): Result<List<Item>> =
        llamar { api.listarPorCategoria(carrera.slug).products.map { it.toItem() } }

    override suspend fun obtenerDetalle(id: Int): Result<Item> =
        llamar { api.obtenerDetalle(id).toItem() }

    /** Envuelve la llamada en Result sin ocultar la cancelacion de la corrutina. */
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
