package com.example.testt.userpreferences

/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface UserDataStore {
    suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean)
    suspend fun getCurrentUserId(): String
}
