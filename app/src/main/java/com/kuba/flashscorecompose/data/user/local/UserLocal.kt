package com.kuba.flashscorecompose.data.user.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.user.local.model.UserEntity
import com.kuba.flashscorecompose.data.userpreferences.local.preferences.UserDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class UserLocal(
    private val roomStorage: RoomStorage,
    private val userDataStore: UserDataStore
) : UserLocalDataSource {

    override fun observeUser(id: String): Flow<UserEntity> {
        return roomStorage.getDatabase().userDao().observeUser(id)
    }

    override suspend fun getUserById(id: String): UserEntity? {
        return roomStorage.getDatabase().userDao().getUserById(id)
    }

    override fun getUserByEmail(email: String): Flow<UserEntity> {
        return roomStorage.getDatabase().userDao().getUserByEmail(email)
    }

    override suspend fun saveUser(user: UserEntity) {
        roomStorage.getDatabase().userDao().saveUser(user)
    }

    override suspend fun deleteUser(id: String) {
        roomStorage.getDatabase().userDao().deleteUser(id)
    }
}