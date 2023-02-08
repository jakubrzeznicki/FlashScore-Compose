package com.kuba.flashscorecompose.data.user.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface UserDataStore {
    fun getIsOnBoardingCompleted(): Flow<Boolean>
    suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean)
}