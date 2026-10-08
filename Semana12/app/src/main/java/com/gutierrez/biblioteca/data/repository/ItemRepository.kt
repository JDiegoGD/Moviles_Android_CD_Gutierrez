package com.gutierrez.biblioteca.data.repository

import com.gutierrez.biblioteca.domain.model.Carrera
import com.gutierrez.biblioteca.domain.model.Item

/**
 * Contrato que usan los ViewModels. Ellos no saben si los datos vienen de
 * Retrofit, de Room o de otra fuente: eso lo decide la implementacion.
 */
interface ItemRepository {
    /** Libros de una carrera. Devuelve Result.failure si falla la red o la API. */
    suspend fun listarPorCarrera(carrera: Carrera): Result<List<Item>>

    /** Detalle de un libro por su id (segundo endpoint). */
    suspend fun obtenerDetalle(id: Int): Result<Item>
}
