package com.gutierrez.biblioteca.data.mapper

import com.gutierrez.biblioteca.data.remote.dto.ItemDto
import com.gutierrez.biblioteca.domain.model.Item

/** Dto (API) -> modelo de dominio. En H8 se agregan las conversiones con Entity (Room). */
fun ItemDto.toItem(): Item = Item(
    id = id,
    titulo = title,
    subtitulo = brand ?: category.orEmpty(),
    imagenUrl = thumbnail
)
