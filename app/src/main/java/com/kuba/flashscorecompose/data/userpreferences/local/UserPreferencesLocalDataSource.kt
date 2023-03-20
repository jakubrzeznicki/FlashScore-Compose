package com.kuba.flashscorecompose.data.userpreferences.local

import com.kuba.flashscorecompose.data.userpreferences.local.model.UserPreferencesEntity

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
    suspend fun getFavoriteTeamIds(): List<Int>?
    suspend fun getFavoritePlayerIds(): List<Int>?
    suspend fun getUserPreferences(): UserPreferencesEntity?
    suspend fun saveUserPreferences(userPreferencesEntity: UserPreferencesEntity)
}