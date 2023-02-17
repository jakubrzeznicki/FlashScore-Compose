package com.kuba.flashscorecompose.data.userpreferences.local

import com.kuba.flashscorecompose.data.userpreferences.local.model.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/02/2023.
 */
interface UserPreferencesLocalDataSource {
    suspend fun getIsOnBoardingCompleted(): Boolean?
    suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean)
    suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean)
    suspend fun getCurrentUserId(): String
    suspend fun saveFavoriteTeamIds(teamIds: List<Int>)
    suspend fun saveFavoritePlayerIds(playerIds: List<Int>)
    suspend fun saveFavoriteFixtureIds(fixtureIds: List<Int>)
    suspend fun getUserPreferences(): UserPreferencesEntity?
    fun observeUserPreferences(currentUserId: String): Flow<UserPreferencesEntity?>
    suspend fun saveUserPreferences(userPreferencesEntity: UserPreferencesEntity)
}