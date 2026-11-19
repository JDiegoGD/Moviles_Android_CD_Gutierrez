package com.gutierrez.campusgps.data

import com.gutierrez.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>

    fun playServicesDisponible(): Boolean
}

class UbicacionNoDisponibleException :
    Exception("No se pudo determinar la ubicación")