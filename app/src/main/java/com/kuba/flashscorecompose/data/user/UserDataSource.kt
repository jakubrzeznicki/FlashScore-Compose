package com.kuba.flashscorecompose.data.user

import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface UserDataSource {
    fun getIsOnBoardingCompleted(): Flow<Boolean>
    suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean)
}