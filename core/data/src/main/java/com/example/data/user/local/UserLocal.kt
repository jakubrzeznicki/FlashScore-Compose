package com.example.data.user.local

import com.example.database.RoomStorage
import com.example.database.user.model.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class UserLocal(private val roomStorage: RoomStorage) : UserLocalDataSource {

    override fun observeUser(id: String): Flow<UserEntity?> {
        return roomStorage.getDatabase().userDao().observeUser(id)
    }

    override suspend fun getUserById(id: String): UserEntity? {
        return roomStorage.getDatabase().userDao().getUserById(id)
    }

    override suspend fun saveUser(user: UserEntity) {
        roomStorage.getDatabase().userDao().saveUser(user)
    }

    override suspend fun deleteUser(id: String) {
        roomStorage.getDatabase().userDao().deleteUser(id)
    }
}