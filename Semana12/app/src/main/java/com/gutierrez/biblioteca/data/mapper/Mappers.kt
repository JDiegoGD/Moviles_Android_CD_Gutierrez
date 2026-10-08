package com.gutierrez.biblioteca.data.mapper

import com.gutierrez.biblioteca.data.local.ItemEntity
import com.gutierrez.biblioteca.data.remote.dto.ItemDto
import com.gutierrez.biblioteca.domain.model.Carrera
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

fun Item.toEntity(carrera: Carrera): ItemEntity = ItemEntity(
    id = id,
    carrera = carrera.name,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    descripcion = descripcion,
    precio = precio,
    valoracion = valoracion,
    esFavorito = esFavorito
)

fun ItemDto.toEntity(carrera: Carrera): ItemEntity = toItem().toEntity(carrera)

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
