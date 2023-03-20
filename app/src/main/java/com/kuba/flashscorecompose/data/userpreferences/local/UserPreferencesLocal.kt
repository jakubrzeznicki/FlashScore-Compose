package com.kuba.flashscorecompose.data.userpreferences.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.userpreferences.local.model.UserPreferencesEntity
import com.kuba.flashscorecompose.data.userpreferences.local.preferences.UserDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/02/2023.
 */
class UserPreferencesLocal(
    private val roomStorage: RoomStorage,
    private val userDataStore: UserDataStore
) : UserPreferencesLocalDataSource {

    override suspend fun getIsOnBoardingCompleted(): Boolean? {
        val currentUserId = userDataStore.getCurrentUserId()
        return roomStorage.getDatabase().userPreferencesDao()
            .getIsOnBoardingCompleted(currentUserId)
    }

    override suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean) {
        userDataStore.saveCurrentUserId(userId, isKeepLogged)
    }

    override suspend fun getCurrentUserId(): String {
        return userDataStore.getCurrentUserId()
    }

    override suspend fun saveFavoriteTeamIds(teamIds: List<Int>) {
        val userPreferencesEntity = getUserPreferences()
        val updateUserPreferencesEntity = userPreferencesEntity?.copy(
            favoriteTeamIds = teamIds
        )
        roomStorage.getDatabase().userPreferencesDao()
            .getUserPreferences(userPreferencesEntity?.userId.orEmpty())
        updateUserPreferencesEntity?.let {
            roomStorage.getDatabase().userPreferencesDao()
                .saveUserPreferences(it)
        }
    }

    override suspend fun saveFavoritePlayerIds(playerIds: List<Int>) {
        val userPreferencesEntity = getUserPreferences()
        val updateUserPreferencesEntity = userPreferencesEntity?.copy(
            favoritePlayerIds = playerIds
        )
        updateUserPreferencesEntity?.let {
            roomStorage.getDatabase().userPreferencesDao()
                .saveUserPreferences(it)
        }
    }

    override suspend fun saveFavoriteFixtureIds(fixtureIds: List<Int>) {
        val userPreferencesEntity = getUserPreferences()
        val updateUserPreferencesEntity = userPreferencesEntity?.copy(
            favoriteFixtureIds = fixtureIds
        )
        updateUserPreferencesEntity?.let {
            roomStorage.getDatabase().userPreferencesDao()
                .saveUserPreferences(it)
        }
    }

    override suspend fun getUserPreferences(): UserPreferencesEntity? {
        val currentUserId = userDataStore.getCurrentUserId()
        return roomStorage.getDatabase().userPreferencesDao().getUserPreferences(currentUserId)
    }

    override fun observeUserPreferences(currentUserId: String): Flow<UserPreferencesEntity?> {
        return roomStorage.getDatabase().userPreferencesDao().observeUserPreferences(currentUserId)
    }

    override suspend fun saveUserPreferences(userPreferencesEntity: UserPreferencesEntity) {
        roomStorage.getDatabase().userPreferencesDao().saveUserPreferences(userPreferencesEntity)
    }
}