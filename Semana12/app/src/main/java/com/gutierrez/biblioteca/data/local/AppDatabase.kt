package com.gutierrez.biblioteca.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Base de datos Room de la app. Si cambia la tabla, hay que subir version. */
@Database(entities = [ItemEntity::class, CarritoEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        private const val NOMBRE = "biblioteca.db"

        fun crear(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, NOMBRE)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
    }
}
