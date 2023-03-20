package com.kuba.flashscorecompose.data.userpreferences.local.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface UserDataStore {
    suspend fun getIsOnBoardingCompleted(): Boolean
    suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean)
    suspend fun getIsKeepLogged(): Boolean
    suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean)
    suspend fun getCurrentUserId(): String
    fun observeCurrentUserId(): Flow<String>
}