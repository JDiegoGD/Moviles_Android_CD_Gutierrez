package com.gutierrez.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gutierrez.huellitasvet.data.local.entity.HorarioAtencion
import kotlinx.coroutines.flow.Flow

@Dao
interface HorarioDao {
    @Query("SELECT * FROM horario_atencion ORDER BY diaSemana")
    fun observarTodos(): Flow<List<HorarioAtencion>>

    @Query("SELECT COUNT(*) FROM horario_atencion")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(h: HorarioAtencion)

    @Insert
    suspend fun insertarTodos(lista: List<HorarioAtencion>)
}
