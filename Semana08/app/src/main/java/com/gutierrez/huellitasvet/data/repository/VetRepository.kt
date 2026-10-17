package com.gutierrez.huellitasvet.data.repository

import com.gutierrez.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

/**
 * El contrato: ViewModels solo conocen esta interface, nunca el DAO.
 *
 * En el proyecto base solo estan las funciones de Servicio (ejemplo resuelto).
 * Las de Usuarios, Mascotas, Citas y Panel se agregan en el Paso 4
 * (cuando ya existen las entidades y relaciones que usan).
 */
interface VetRepository {
    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
}
