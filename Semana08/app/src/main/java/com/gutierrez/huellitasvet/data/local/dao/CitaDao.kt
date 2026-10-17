package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.ConteoServicio
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {

    // ---------- Escritura ----------
    @Insert
    suspend fun insertar(c: Cita): Long

    @Query("UPDATE citas SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)

    /** Pone estado ATENDIDA y guarda diagnostico, tratamiento y peso. */
    @Query(
        "UPDATE citas SET estado = 'ATENDIDA', diagnostico = :diagnostico, " +
            "tratamiento = :tratamiento, pesoRegistrado = :peso WHERE id = :id"
    )
    suspend fun registrarAtencion(id: Int, diagnostico: String, tratamiento: String, peso: Double)

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun obtener(id: Int): Cita?

    // ---------- Horarios ----------
    /** Cuenta las citas NO canceladas con esa fecha y hora. */
    @Query("SELECT COUNT(*) FROM citas WHERE fecha = :fecha AND hora = :hora AND estado != 'CANCELADA'")
    suspend fun contarOcupadas(fecha: String, hora: String): Int

    @Query("SELECT hora FROM citas WHERE fecha = :fecha AND estado != 'CANCELADA'")
    fun observarHorasOcupadas(fecha: String): Flow<List<String>>

    // ---------- Lectura (Flow de CitaConDetalle) ----------
    @Transaction
    @Query(
        "SELECT c.* FROM citas c INNER JOIN mascotas m ON c.mascotaId = m.id " +
            "WHERE m.duenoId = :duenoId ORDER BY c.fecha, c.hora"
    )
    fun observarPorDueno(duenoId: Int): Flow<List<CitaConDetalle>>

    @Transaction
    @Query("SELECT * FROM citas WHERE fecha = :fecha ORDER BY hora")
    fun observarPorFecha(fecha: String): Flow<List<CitaConDetalle>>

    @Transaction
    @Query("SELECT * FROM citas WHERE id = :id")
    fun observarUna(id: Int): Flow<CitaConDetalle?>

    /** Solo ATENDIDAS, la mas reciente primero. */
    @Transaction
    @Query(
        "SELECT * FROM citas WHERE mascotaId = :mascotaId AND estado = 'ATENDIDA' " +
            "ORDER BY fecha DESC, hora DESC"
    )
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>

    // ---------- Conteos de apoyo (reglas de eliminacion) ----------
    @Query("SELECT COUNT(*) FROM citas WHERE servicioId = :servicioId")
    suspend fun contarCitasDeServicio(servicioId: Int): Int

    // ---------- Panel ----------
    @Query("SELECT COUNT(*) FROM citas WHERE fecha = :fecha AND estado != 'CANCELADA'")
    fun contarPorFecha(fecha: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM citas WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    @Query(
        "SELECT s.nombre AS nombre, COUNT(c.id) AS total FROM citas c " +
            "INNER JOIN servicios s ON c.servicioId = s.id " +
            "GROUP BY s.id ORDER BY s.nombre"
    )
    fun contarPorServicio(): Flow<List<ConteoServicio>>

    @Query(
        "SELECT COALESCE(SUM(precio), 0.0) FROM citas " +
            "WHERE estado = 'ATENDIDA' AND fecha >= :fechaInicio"
    )
    fun ingresosDesde(fechaInicio: String): Flow<Double>
}
