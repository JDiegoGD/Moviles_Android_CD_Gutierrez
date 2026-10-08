package com.gutierrez.biblioteca.data.remote.dto

/**
 * Producto tal como lo responde dummyjson.com (solo los campos que usa la app;
 * Gson ignora el resto). Los campos que la API puede omitir son nulos.
 */
data class ItemDto(
    val id: Int,
    val title: String,
    val description: String?,
    val category: String?,
    val price: Double?,
    val rating: Double?,
    val stock: Int?,
    val brand: String?,
    val thumbnail: String?,
    val images: List<String>?
)

/** Respuesta paginada de GET products/category/{slug}. */
data class ItemsResponseDto(
    val products: List<ItemDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
