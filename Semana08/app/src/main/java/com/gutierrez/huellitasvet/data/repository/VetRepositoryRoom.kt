package com.gutierrez.huellitasvet.data.repository

import com.gutierrez.huellitasvet.data.local.VetDatabase
import com.gutierrez.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    // ---------- Servicios (ejemplo resuelto) ----------
    override fun observarServicios(): Flow<List<Servicio>> = db.servicioDao().observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = db.servicioDao().observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        // id = 0 -> servicio nuevo; id > 0 -> actualiza el existente
        if (s.id == 0) db.servicioDao().insertar(s) else db.servicioDao().actualizar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) {
        db.servicioDao().eliminar(s)
    }

    override suspend fun cambiarActivo(id: Int, activo: Boolean) {
        db.servicioDao().cambiarActivo(id, activo)
    }

    // TODO (Paso 4): Usuarios, Mascotas, Citas y Panel
}
