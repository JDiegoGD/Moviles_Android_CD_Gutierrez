package com.gutierrez.biblioteca

import android.content.Context
import com.gutierrez.biblioteca.data.local.AppDatabase
import com.gutierrez.biblioteca.data.remote.RetrofitClient
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.data.repository.ItemRepositoryImpl

/**
 * Contenedor de dependencias manual: crea la API, la base de datos y los
 * repositorios en un solo lugar. En H12 agrega Firebase.
 */
class AppContainer(context: Context) {

    // Se crea la primera vez que se usa (cuando se pide el repositorio).
    val database: AppDatabase by lazy { AppDatabase.crear(context) }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(api = RetrofitClient.api, dao = database.itemDao())
    }
}
