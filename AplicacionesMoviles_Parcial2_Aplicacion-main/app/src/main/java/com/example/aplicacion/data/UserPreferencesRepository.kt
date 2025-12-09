package com.example.aplicacion.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Configuración global de DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

/**
 * Claves para DataStore
 */
private object PrefsKeys {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val USER_EMAIL = stringPreferencesKey("user_email")
}

/**
 * Repositorio encargado de leer y escribir la información de sesión en DataStore.
 */
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    // Flujo para leer login + email
    val userFlow: Flow<Pair<Boolean, String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val isLoggedIn = preferences[PrefsKeys.IS_LOGGED_IN] ?: false
            val email = preferences[PrefsKeys.USER_EMAIL] ?: ""
            Pair(isLoggedIn, email)
        }

    /**
     * Guarda sesión activa + email
     */
    suspend fun saveLoginState(email: String) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.IS_LOGGED_IN] = true
            preferences[PrefsKeys.USER_EMAIL] = email
        }
    }

    /**
     * Cierra sesión
     */
    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}