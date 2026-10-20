package com.gutierrez.huellitasvet.data.repository

import androidx.room.withTransaction
import com.gutierrez.huellitasvet.data.local.VetDatabase
import com.gutierrez.huellitasvet.data.local.entity.Cita
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.entity.Servicio
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.data.local.entity.Vacuna
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.local.relation.MascotaConDueno
import com.gutierrez.huellitasvet.model.ConteoServicio
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.PuntoPeso
import kotlinx.coroutines.flow.Flow

class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    // ---------- Usuarios ----------
    override suspend fun login(correo: String, clave: String): Usuario? =
        db.usuarioDao().login(correo, clave)

    override suspend fun obtenerUsuario(id: Int): Usuario? = db.usuarioDao().obtener(id)

    override suspend fun registrarUsuario(usuario: Usuario): Long =
        if (db.usuarioDao().existeCorreo(usuario.correo) > 0) -1L
        else db.usuarioDao().insertar(usuario)

    // ---------- Servicios ----------
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

    override suspend fun servicioTieneCitas(id: Int): Boolean =
        db.citaDao().contarCitasDeServicio(id) > 0

    // ---------- Mascotas ----------
    override fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>> =
        db.mascotaDao().observarPorDueno(duenoId)

    override fun observarMascota(id: Int): Flow<Mascota?> = db.mascotaDao().observarUna(id)

    override fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>> =
        db.mascotaDao().observarTodas()

    override suspend fun guardarMascota(m: Mascota) {
        // id = 0 -> inserta una nueva; id > 0 -> actualiza la existente
        if (m.id == 0) db.mascotaDao().insertar(m) else db.mascotaDao().actualizar(m)
    }

    override suspend fun eliminarMascota(m: Mascota) {
        db.withTransaction {
            db.vacunaDao().eliminarPorMascota(m.id)
            db.mascotaDao().eliminar(m)
        }
    }

    override suspend fun mascotaTieneCitas(id: Int): Boolean =
        db.mascotaDao().contarCitas(id) > 0

    // ---------- Vacunas ----------
    override fun observarVacunas(mascotaId: Int): Flow<List<Vacuna>> =
        db.vacunaDao().observarPorMascota(mascotaId)

    override suspend fun guardarVacuna(v: Vacuna) {
        db.vacunaDao().insertar(v)
    }

    override suspend fun eliminarVacuna(v: Vacuna) {
        db.vacunaDao().eliminar(v)
    }

    // ---------- Citas ----------
    override fun observarHorasOcupadas(fecha: String): Flow<List<String>> =
        db.citaDao().observarHorasOcupadas(fecha)

    override suspend fun crearCita(
        mascotaId: Int, servicioId: Int, fecha: String, hora: String, nota: String
    ): Long {
        // Doble seguro: dos citas no canceladas no pueden compartir fecha y hora
        if (db.citaDao().contarOcupadas(fecha, hora) > 0) return -1L
        // Copiamos el precio del servicio en este momento ("foto" del precio)
        val servicio = db.servicioDao().obtener(servicioId) ?: return -1L
        val cita = Cita(
            mascotaId = mascotaId,
            servicioId = servicioId,
            fecha = fecha,
            hora = hora,
            nota = nota,
            estado = EstadoCita.PENDIENTE.name,
            precio = servicio.precio,
            fechaCreacion = System.currentTimeMillis()
        )
        return db.citaDao().insertar(cita)
    }

    override fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>> =
        db.citaDao().observarPorDueno(duenoId)

    override fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>> =
        db.citaDao().observarPorFecha(fecha)

    override fun observarCita(id: Int): Flow<CitaConDetalle?> = db.citaDao().observarUna(id)

    override fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>> =
        db.citaDao().observarHistorial(mascotaId)

    override fun observarPesos(mascotaId: Int): Flow<List<PuntoPeso>> =
        db.citaDao().observarPesos(mascotaId)

    override suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita) {
        db.citaDao().actualizarEstado(id, estado.name)
    }

    override suspend fun reprogramarCita(id: Int, fecha: String, hora: String): Boolean {
        if (db.citaDao().contarOcupadas(fecha, hora) > 0) return false
        db.citaDao().reprogramar(id, fecha, hora)
        return true
    }

    override suspend fun registrarAtencion(
        citaId: Int, diagnostico: String, tratamiento: String, peso: Double
    ) {
        // Una sola transaccion: si algo falla, no queda nada a medias
        db.withTransaction {
            val cita = db.citaDao().obtener(citaId) ?: return@withTransaction
            db.citaDao().registrarAtencion(citaId, diagnostico, tratamiento, peso)
            db.mascotaDao().actualizarPeso(cita.mascotaId, peso)
        }
    }

    // ---------- Panel ----------
    override fun observarCitasDeFecha(fecha: String): Flow<Int> = db.citaDao().contarPorFecha(fecha)

    override fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int> =
        db.citaDao().contarPorEstado(estado.name)

    override fun observarCitasPorServicio(): Flow<List<ConteoServicio>> =
        db.citaDao().contarPorServicio()

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> =
        db.citaDao().ingresosDesde(fechaInicio)
}
