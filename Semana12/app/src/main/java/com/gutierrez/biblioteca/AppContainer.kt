package com.gutierrez.biblioteca

import com.gutierrez.biblioteca.data.remote.RetrofitClient
import com.gutierrez.biblioteca.data.repository.ItemRepository
import com.gutierrez.biblioteca.data.repository.ItemRepositoryImpl

/**
 * Contenedor de dependencias manual: crea la API y los repositorios en un solo
 * lugar. En H8 agrega la base de datos Room y en H12 Firebase.
 */
class AppContainer {
    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(api = RetrofitClient.api)
    }
}
