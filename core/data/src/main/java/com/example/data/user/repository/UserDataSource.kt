package com.example.data.user.repository

import com.example.model.user.User
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface UserDataSource {
    fun observeUser(id: String): Flow<User?>
    suspend fun getUserById(id: String): User?
    suspend fun saveUser(user: User)
    suspend fun deleteUser(id: String)
}
