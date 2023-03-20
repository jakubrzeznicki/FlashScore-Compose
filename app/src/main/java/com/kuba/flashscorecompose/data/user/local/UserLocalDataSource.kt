package com.kuba.flashscorecompose.data.user.local

import com.kuba.flashscorecompose.data.user.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface UserLocalDataSource {
    suspend fun getIsOnBoardingCompleted(): Boolean
    suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean)
    suspend fun getIsKeepLogged(): Boolean
    suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean)
    suspend fun getCurrentUserId(): String
    fun observeUser(id: String): Flow<UserEntity>
    suspend fun getUserById(id: String): UserEntity?
    fun getUserByEmail(email: String): Flow<UserEntity>
    suspend fun saveUser(user: UserEntity)
}