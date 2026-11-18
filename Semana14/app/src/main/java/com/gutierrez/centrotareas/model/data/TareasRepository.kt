package com.gutierrez.centrotareas.model.data

import com.gutierrez.centrotareas.model.PrimosProgreso
import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
    fun contarPrimos(hasta: Int): Flow<PrimosProgreso>
    fun observarProgreso(): Flow<Float>
}
