package com.kuba.flashscorecompose.data.userpreferences

import com.kuba.flashscorecompose.data.userpreferences.local.UserPreferencesLocalDataSource
import com.kuba.flashscorecompose.data.userpreferences.mapper.toUserPreferences
import com.kuba.flashscorecompose.data.userpreferences.mapper.toUserPreferencesEntity
import com.kuba.flashscorecompose.data.userpreferences.model.UserPreferences
import com.kuba.flashscorecompose.notifications.model.NotificationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Created by jrzeznicki on 10/02/2023.
 */
class UserPreferencesRepository(private val local: UserPreferencesLocalDataSource) :
    UserPreferencesDataSource {
    override suspend fun getIsOnBoardingCompleted(): Boolean? {
        return local.getIsOnBoardingCompleted()
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

    override suspend fun saveFavoriteFixturesIds(fixtureIds: List<Int>) {
        local.saveFavoriteFixtureIds(fixtureIds)
    }

    override suspend fun saveReminder(notificationData: NotificationData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReminder(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getReminders(timestamp: Long): List<NotificationData> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserPreferences(): UserPreferences? {
        return local.getUserPreferences()?.toUserPreferences()
    }

    override fun observeUserPreferences(currentUserId: String): Flow<UserPreferences> {
        return local.observeUserPreferences(currentUserId).mapNotNull { it?.toUserPreferences() }
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        local.saveUserPreferences(userPreferences.toUserPreferencesEntity())
    }
}