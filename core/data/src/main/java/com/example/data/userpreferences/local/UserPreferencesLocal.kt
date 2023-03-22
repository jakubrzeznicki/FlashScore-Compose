package com.example.data.userpreferences.local

import com.example.database.RoomStorage
import com.example.database.userpreferences.model.UserPreferencesEntity
import com.example.datastore.userpreferences.UserDataStore
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
        updateUserPreferencesEntity?.saveData()
    }

    override suspend fun saveFavoritePlayerIds(playerIds: List<Int>) {
        val userPreferencesEntity = getUserPreferences()
        val updateUserPreferencesEntity = userPreferencesEntity?.copy(
            favoritePlayerIds = playerIds
        )
        updateUserPreferencesEntity?.saveData()
    }

    override suspend fun saveFavoriteFixtureIds(fixtureIds: List<Int>) {
        val userPreferencesEntity = getUserPreferences()
        val updateUserPreferencesEntity = userPreferencesEntity?.copy(
            favoriteFixtureIds = fixtureIds
        )
        updateUserPreferencesEntity?.saveData()
    }

    private suspend fun UserPreferencesEntity?.saveData() {
        this?.let {
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
