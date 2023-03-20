package com.kuba.flashscorecompose.data.userpreferences

import com.kuba.flashscorecompose.data.userpreferences.local.UserPreferencesLocalDataSource
import com.kuba.flashscorecompose.data.userpreferences.mapper.toUserPreferences
import com.kuba.flashscorecompose.data.userpreferences.mapper.toUserPreferencesEntity
import com.kuba.flashscorecompose.data.userpreferences.model.UserPreferences

/**
 * Created by jrzeznicki on 10/02/2023.
 */
class UserPreferencesRepository(private val local: UserPreferencesLocalDataSource) :
    UserPreferencesDataSource {
    override suspend fun getIsOnBoardingCompleted(): Boolean? {
        return local.getIsOnBoardingCompleted()
    }

    override suspend fun saveIsOnBoardingCompleted(isOnBoardingCompleted: Boolean) {
        local.saveIsOnBoardingCompleted(isOnBoardingCompleted)
    }

    override suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean) {
        local.saveCurrentUserId(userId, isKeepLogged)
    }

    override suspend fun getCurrentUserId(): String {
        return local.getCurrentUserId()
    }

    override suspend fun saveFavoriteTeamIds(teamIds: List<Int>) {
        local.saveFavoriteTeamIds(teamIds)
    }

    override suspend fun saveFavoritePlayerIds(playerIds: List<Int>) {
        local.saveFavoritePlayerIds(playerIds)
    }

    override suspend fun getFavoriteTeamIds(): List<Int>? {
        return local.getFavoriteTeamIds()
    }

    override suspend fun getFavoritePlayerIds(): List<Int>? {
        return local.getFavoritePlayerIds()
    }

    override suspend fun getUserPreferences(): UserPreferences? {
        return local.getUserPreferences()?.toUserPreferences()
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        local.saveUserPreferences(userPreferences.toUserPreferencesEntity())
    }
}