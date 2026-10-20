package com.gutierrez.huellitasvet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gutierrez.huellitasvet.data.local.dao.CitaDao
import com.gutierrez.huellitasvet.data.local.dao.MascotaDao
import com.gutierrez.huellitasvet.data.local.dao.ServicioDao
import com.gutierrez.huellitasvet.data.local.dao.UsuarioDao
import com.gutierrez.huellitasvet.data.local.dao.VacunaDao
import com.gutierrez.huellitasvet.data.local.entity.Cita
import com.gutierrez.huellitasvet.data.local.entity.Mascota
import com.gutierrez.huellitasvet.data.local.entity.Servicio
import com.gutierrez.huellitasvet.data.local.entity.Usuario
import com.gutierrez.huellitasvet.data.local.entity.Vacuna

@Database(
    entities = [Servicio::class, Usuario::class, Mascota::class, Cita::class, Vacuna::class],
    version = 4, // subio de 3 a 4: se agrego la entidad Vacuna
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {
    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun citaDao(): CitaDao
    abstract fun vacunaDao(): VacunaDao

    companion object {
        @Volatile
        private var INSTANCE: VetDatabase? = null

        // Singleton: una sola instancia de la base para toda la app
        fun obtener(context: Context): VetDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                VetDatabase::class.java,
                "huellitasvet.db"
            )
                // Solo para el laboratorio: si el esquema cambia, recrea la base
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}
