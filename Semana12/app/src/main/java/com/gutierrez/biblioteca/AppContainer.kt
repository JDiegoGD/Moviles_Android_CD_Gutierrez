package com.gutierrez.biblioteca

import android.content.Context
import com.gutierrez.biblioteca.data.auth.AuthRepository
import com.gutierrez.biblioteca.data.auth.FirebaseAuthRepository
import com.gutierrez.biblioteca.data.local.AppDatabase
import com.gutierrez.biblioteca.data.remote.RetrofitClient
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.data.repository.ItemRepositoryImpl
import com.gutierrez.biblioteca.notification.Notificador
import com.gutierrez.biblioteca.notification.NotificadorAndroid

/**
 * Contenedor de dependencias manual: crea la API, la base de datos y los
 * repositorios en un solo lugar.
 */
class AppContainer(context: Context) {

    // Se crea la primera vez que se usa (cuando se pide el repositorio).
    val database: AppDatabase by lazy { AppDatabase.crear(context) }

    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }

    val notificador: Notificador by lazy { NotificadorAndroid(context.applicationContext) }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(api = RetrofitClient.api, dao = database.itemDao())
    }
}
