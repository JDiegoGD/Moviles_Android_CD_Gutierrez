package com.gutierrez.huellitasvet.data.repository

import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.entity.Servicio
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.data.local.relation.CitaConDetalle
import com.gutierrez.huellitasvet.data.local.relation.MascotaConDueno
import com.gutierrez.huellitasvet.model.ConteoServicio
import com.gutierrez.huellitasvet.model.EstadoCita
import com.gutierrez.huellitasvet.model.PuntoPeso
import kotlinx.coroutines.flow.Flow

/** El contrato: ViewModels solo conocen esta interface, nunca el DAO. */
interface VetRepository {
    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun obtenerUsuario(id: Int): Usuario?
    /** Crea el usuario y devuelve su id, o -1 si el correo ya esta registrado. */
    suspend fun registrarUsuario(usuario: Usuario): Long

    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneCitas(id: Int): Boolean

    // Mascotas
    fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>>
    fun observarMascota(id: Int): Flow<Mascota?>
    fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>>
    suspend fun guardarMascota(m: Mascota)
    suspend fun eliminarMascota(m: Mascota)
    suspend fun mascotaTieneCitas(id: Int): Boolean

    // Citas
    fun observarHorasOcupadas(fecha: String): Flow<List<String>>
    suspend fun crearCita(
        mascotaId: Int, servicioId: Int, fecha: String,
        hora: String, nota: String
    ): Long
    fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>>
    fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>>
    fun observarCita(id: Int): Flow<CitaConDetalle?>
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>
    fun observarPesos(mascotaId: Int): Flow<List<PuntoPeso>>
    suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita)
    suspend fun registrarAtencion(
        citaId: Int, diagnostico: String,
        tratamiento: String, peso: Double
    )

    // Panel
    fun observarCitasDeFecha(fecha: String): Flow<Int>
    fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int>
    fun observarCitasPorServicio(): Flow<List<ConteoServicio>>
    fun observarIngresosDesde(fechaInicio: String): Flow<Double>
}
