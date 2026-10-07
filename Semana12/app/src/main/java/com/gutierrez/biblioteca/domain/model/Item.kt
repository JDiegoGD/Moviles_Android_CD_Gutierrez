package com.gutierrez.biblioteca.domain.model

/**
 * Modelo que usa la UI. Es independiente de la API (ItemDto) y de Room (ItemEntity):
 * los mappers se encargan de convertir hacia este modelo.
 */
data class Item(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)
