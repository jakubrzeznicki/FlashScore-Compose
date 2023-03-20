package com.kuba.flashscorecompose.data.user

import android.util.Log
import com.kuba.flashscorecompose.data.user.local.UserLocalDataSource
import com.kuba.flashscorecompose.data.user.mapper.toUser
import com.kuba.flashscorecompose.data.user.mapper.toUserEntity
import com.kuba.flashscorecompose.data.user.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class UserRepository(private val local: UserLocalDataSource) : UserDataSource {
    override suspend fun getIsOnBoardingCompleted(): Boolean {
        return local.getIsOnBoardingCompleted()
    }

    override suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean) {
        local.saveIsOnBoardingCompleted(isOnBoardingCompleted)
    }

    override suspend fun getIsKeepLogged(): Boolean {
        return local.getIsKeepLogged()
    }

    override suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean) {
        local.saveCurrentUserId(userId, isKeepLogged)
    }

    override suspend fun getCurrentUserId(): String {
        return local.getCurrentUserId()
    }

    override fun observeUser(id: String): Flow<User> {
        return local.observeUser(id).map { it.toUser() }
    }

    override suspend fun getUserById(id: String): User? {
        return local.getUserById(id)?.toUser()
    }

    override fun getUserByEmail(email: String): Flow<User> {
        return local.getUserByEmail(email).map { it.toUser() }
    }

    override suspend fun saveUser(user: User) {
        Log.d("TEST_LOG", "saveUser - $user")
        local.saveUser(user.toUserEntity())
    }
}