package com.gutierrez.biblioteca.domain.model

/**
 * Modelo que usa la UI. Es independiente de la API (ItemDto) y de Room (ItemEntity):
 * los mappers se encargan de convertir hacia este modelo.
 *
 * descripcion, precio y valoracion se muestran en el detalle (H7).
 */
data class Item(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    val descripcion: String = "",
    val precio: Double? = null,
    val valoracion: Double? = null
)
