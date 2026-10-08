package com.gutierrez.biblioteca.data.repository

import com.gutierrez.biblioteca.data.local.ItemDao
import com.gutierrez.biblioteca.data.mapper.toEntity
import com.gutierrez.biblioteca.data.mapper.toItem
import com.gutierrez.biblioteca.data.remote.ApiService
import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** La API alimenta a Room y la UI lee siempre de Room (fuente unica de verdad). */
class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override fun observarPorCarrera(carrera: Carrera): Flow<List<Item>> =
        dao.porCarrera(carrera.name).map { entidades -> entidades.map { it.toItem() } }

    override fun observarFavoritos(): Flow<List<Item>> =
        dao.favoritos().map { entidades -> entidades.map { it.toItem() } }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)

    override suspend fun refrescar(carrera: Carrera): Result<Unit> = llamar {
        val remotos = api.listarPorCategoria(carrera.slug).products
        // @Upsert reemplaza la fila completa: se conservan los favoritos ya marcados.
        val favoritos = dao.idsFavoritos().toSet()
        dao.guardarTodos(
            remotos.map { it.toEntity(carrera).copy(esFavorito = it.id in favoritos) }
        )
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> {
        val local = dao.porId(id)
        return llamar { api.obtenerDetalle(id).toItem().copy(esFavorito = local?.esFavorito ?: false) }
            .recoverCatching { error -> local?.toItem() ?: throw error }
    }

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
