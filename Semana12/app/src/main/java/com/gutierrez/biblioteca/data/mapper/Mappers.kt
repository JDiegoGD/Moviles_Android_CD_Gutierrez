package com.gutierrez.biblioteca.data.mapper

import com.gutierrez.biblioteca.data.local.ItemEntity
import com.gutierrez.biblioteca.data.remote.dto.ItemDto
import com.gutierrez.biblioteca.domain.model.Categoria
import com.gutierrez.biblioteca.domain.model.Item

/** Conversiones Dto (API) -> Entity (Room) -> modelo de dominio (UI). */

fun ItemDto.toItem(): Item = Item(
    id = id,
    titulo = title,
    subtitulo = brand ?: category.orEmpty(),
    imagenUrl = thumbnail,
    descripcion = description.orEmpty(),
    precio = price,
    valoracion = rating
)

fun Item.toEntity(categoria: Categoria): ItemEntity = ItemEntity(
    id = id,
    categoria = categoria.name,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    descripcion = descripcion,
    precio = precio,
    valoracion = valoracion,
    esFavorito = esFavorito
)

fun ItemDto.toEntity(categoria: Categoria): ItemEntity = toItem().toEntity(categoria)

fun ItemEntity.toItem(): Item = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito,
    descripcion = descripcion,
    precio = precio,
    valoracion = valoracion
)
