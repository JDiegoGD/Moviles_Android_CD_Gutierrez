package com.gutierrez.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtener(id: Int): Usuario?

    @Query("SELECT COUNT(*) FROM usuarios WHERE LOWER(correo) = LOWER(:correo)")
    suspend fun existeCorreo(correo: String): Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE LOWER(codigoAdmin) = LOWER(:codigo)")
    suspend fun existeCodigoAdmin(codigo: String): Int

    @Query("SELECT * FROM usuarios")
    fun observarTodos(): Flow<List<Usuario>>

    @Query("SELECT COUNT(*) FROM usuarios WHERE rol = :rol")
    fun observarCantidadPorRol(rol: String): Flow<Int>

    @Insert
    suspend fun insertar(usuario: Usuario): Long

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    // Borrado en orden: primero lo que depende del usuario (vacunas, citas, mascotas) y al final el usuario
    @Query("DELETE FROM vacunas WHERE mascotaId IN (SELECT id FROM mascotas WHERE duenoId = :id)")
    suspend fun eliminarVacunasDeDueno(id: Int)

    @Query("DELETE FROM citas WHERE mascotaId IN (SELECT id FROM mascotas WHERE duenoId = :id)")
    suspend fun eliminarCitasDeDueno(id: Int)

    @Query("DELETE FROM mascotas WHERE duenoId = :id")
    suspend fun eliminarMascotasDeDueno(id: Int)

    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun eliminarUsuario(id: Int)
}
