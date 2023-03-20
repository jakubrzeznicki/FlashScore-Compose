package com.kuba.flashscorecompose.data.user.local.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class DefaultUserDataStore(private val dataStore: DataStore<Preferences>) : UserDataStore {

    override suspend fun getIsOnBoardingCompleted(): Boolean {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                Log.d("TEST_LOG", "getIsOnBoardingCompleted  in datastore")
                preferences[PreferencesKeys.IS_ON_BOARDING_COMPLETED] ?: false
            }.first()
    }

    override suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean) {
        dataStore.edit { preferences ->
            Log.d("TEST_LOG", "saveIsOnBoardingCompleted - $isOnBoardingCompleted in datastore")
            preferences[PreferencesKeys.IS_ON_BOARDING_COMPLETED] = isOnBoardingCompleted
        }
    }

    override suspend fun getIsKeepLogged(): Boolean {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[PreferencesKeys.IS_KEEP_LOGGED] ?: false
            }.first()
    }

    override suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_USER_ID] = userId
            preferences[PreferencesKeys.IS_KEEP_LOGGED] = isKeepLogged
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
        val IS_ON_BOARDING_COMPLETED = booleanPreferencesKey("is_on_boarding_completed")
        val IS_KEEP_LOGGED = booleanPreferencesKey("is_keep_logged")
        val CURRENT_USER_ID = stringPreferencesKey("current_user_id")
    }
}