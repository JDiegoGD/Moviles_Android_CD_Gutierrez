package com.gutierrez.biblioteca.data.remote

import com.gutierrez.biblioteca.data.remote.dto.ItemDto
import com.gutierrez.biblioteca.data.remote.dto.ItemsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

/** Los dos endpoints de dummyjson.com que usa la app: lista y detalle. */
interface ApiService {

    /** Lista de productos de una categoria. Ejemplo: products/category/laptops */
    @GET("products/category/{slug}")
    suspend fun listarPorCategoria(@Path("slug") slug: String): ItemsResponseDto

    /** Detalle de un producto. Ejemplo: products/1 */
    @GET("products/{id}")
    suspend fun obtenerDetalle(@Path("id") id: Int): ItemDto
}
