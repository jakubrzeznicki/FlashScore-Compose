package com.kuba.flashscorecompose.data.user

import com.kuba.flashscorecompose.data.user.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface UserDataSource {
    suspend fun getIsOnBoardingCompleted(): Boolean
    suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean)
    suspend fun getIsKeepLogged(): Boolean
    suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean)
    suspend fun getCurrentUserId(): String
    fun observeUser(id: String): Flow<User>
    suspend fun getUserById(id: String): User?
    fun getUserByEmail(email: String): Flow<User>
    suspend fun saveUser(user: User)
    suspend fun deleteUser(id: String)
}