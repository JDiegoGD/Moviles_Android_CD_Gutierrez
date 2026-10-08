package com.gutierrez.biblioteca.data.repository

import com.gutierrez.biblioteca.data.mapper.toItem
import com.gutierrez.biblioteca.data.remote.ApiService
import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item
import kotlin.coroutines.cancellation.CancellationException

/** H6: los datos salen directo de la API. En H9 Room pasa a ser la fuente de verdad. */
class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    override suspend fun listarPorCarrera(carrera: Carrera): Result<List<Item>> =
        try {
            val respuesta = api.listarPorCategoria(carrera.slug)
            Result.success(respuesta.products.map { it.toItem() })
        } catch (e: CancellationException) {
            throw e // no ocultar la cancelacion de la corrutina
        } catch (e: Exception) {
            Result.failure(e)
        }
}
