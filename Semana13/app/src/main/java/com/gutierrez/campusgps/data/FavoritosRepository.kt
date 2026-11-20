package com.gutierrez.campusgps.data

import kotlinx.coroutines.flow.Flow

interface FavoritosRepository {
    val favoritos: Flow<Set<Int>>
    suspend fun alternarFavorito(id: Int)
}
