package com.example.data.user.repository

import com.example.data.user.local.UserLocalDataSource
import com.example.data.user.mapper.toUserEntity
import com.example.database.user.mapper.toUser
import com.example.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class UserRepository(private val local: UserLocalDataSource) : UserDataSource {

    override fun observeUser(id: String): Flow<User?> {
        return local.observeUser(id).map { it?.toUser() }
    }

    override suspend fun getUserById(id: String): User? {
        return local.getUserById(id)?.toUser()
    }

    override suspend fun saveUser(user: User) {
        local.saveUser(user.toUserEntity())
    }

    override suspend fun deleteUser(id: String) {
        local.deleteUser(id)
    }
}