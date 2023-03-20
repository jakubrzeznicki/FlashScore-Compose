package com.kuba.flashscorecompose.data.userpreferences

import com.kuba.flashscorecompose.data.userpreferences.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/02/2023.
 */
interface UserPreferencesDataSource {
    suspend fun getIsOnBoardingCompleted(): Boolean?
    suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean)
    suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean)
    suspend fun getCurrentUserId(): String
    fun observeCurrentUserId(): Flow<String>
    suspend fun saveFavoriteTeamIds(teamIds: List<Int>)
    suspend fun saveFavoritePlayerIds(playerIds: List<Int>)
    suspend fun saveFavoriteFixturesIds(fixtureIds: List<Int>)
    suspend fun getUserPreferences(): UserPreferences?
    fun observeUserPreferences(currentUserId: String): Flow<UserPreferences>
    suspend fun saveUserPreferences(userPreferences: UserPreferences)
}