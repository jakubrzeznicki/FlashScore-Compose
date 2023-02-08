package com.kuba.flashscorecompose.data.user

import com.kuba.flashscorecompose.data.user.preferences.UserDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class UserRepository(private val userDataStore: UserDataStore) : UserDataSource {
    override fun getIsOnBoardingCompleted(): Flow<Boolean> {
        return userDataStore.getIsOnBoardingCompleted()
    }

    override suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean) {
        userDataStore.saveIsOnBoardingCompleted(isOnBoardingCompleted)
    }
}