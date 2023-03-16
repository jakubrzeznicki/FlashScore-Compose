package com.example.testt.userpreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class DefaultUserDataStore(private val dataStore: DataStore<Preferences>) : UserDataStore {

    override suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_USER_ID] = userId
        }
    }

    override suspend fun getCurrentUserId(): String {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[PreferencesKeys.CURRENT_USER_ID].orEmpty()
            }.first()
    }

    private object PreferencesKeys {
        val CURRENT_USER_ID = stringPreferencesKey("current_user_id")
    }
}
