package com.gutierrez.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favoritos")

class FavoritosRepositoryPreferences(
    context: Context
) : FavoritosRepository {

    private val applicationContext = context.applicationContext

    private companion object {
        val FAVORITOS_KEY = stringSetPreferencesKey("favoritos_ids")
    }

    override val favoritos: Flow<Set<Int>> = applicationContext.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[FAVORITOS_KEY]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }

    override suspend fun alternarFavorito(id: Int) {
        applicationContext.dataStore.edit { preferences ->
            val actuales = preferences[FAVORITOS_KEY]?.toMutableSet() ?: mutableSetOf()
            val idStr = id.toString()
            if (actuales.contains(idStr)) {
                actuales.remove(idStr)
            } else {
                actuales.add(idStr)
            }
            preferences[FAVORITOS_KEY] = actuales
        }
    }
}
