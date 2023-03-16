package com.example.data.user.local

import com.example.database.user.model.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface UserLocalDataSource {
    fun observeUser(id: String): Flow<UserEntity>
    suspend fun getUserById(id: String): UserEntity?
    suspend fun saveUser(user: UserEntity)
    suspend fun deleteUser(id: String)
}
