package com.kuba.flashscorecompose.data.user.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class DefaultUserDataStore(private val dataStore: DataStore<Preferences>) : UserDataStore {

    override fun getIsOnBoardingCompleted(): Flow<Boolean> {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                Log.d("TEST_LOG","getIsOnBoardingCompleted  in datastore" )
                preferences[PreferencesKeys.IS_ON_BOARDING_COMPLETED] ?: false
            }
    }

    override suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean) {
        dataStore.edit { preferences ->
            Log.d("TEST_LOG","saveIsOnBoardingCompleted - $isOnBoardingCompleted in datastore" )
            preferences[PreferencesKeys.IS_ON_BOARDING_COMPLETED] = isOnBoardingCompleted
        }
    }

    private object PreferencesKeys {
        val IS_ON_BOARDING_COMPLETED = booleanPreferencesKey("is_on_boarding_completed")
    }
}