package com.kuba.flashscorecompose.data.userpreferences

import com.kuba.flashscorecompose.data.userpreferences.model.UserPreferences
import com.kuba.flashscorecompose.notifications.model.NotificationData
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/02/2023.
 */
interface UserPreferencesDataSource {
    suspend fun getIsOnBoardingCompleted(): Boolean?
    suspend fun saveCurrentUserId(userId: String, isKeepLogged: Boolean)
    suspend fun getCurrentUserId(): String
    suspend fun saveFavoriteTeamIds(teamIds: List<Int>)
    suspend fun saveFavoritePlayerIds(playerIds: List<Int>)
    suspend fun saveFavoriteFixturesIds(fixtureIds: List<Int>)
    suspend fun saveReminder(notificationData: NotificationData)
    suspend fun deleteReminder(id: Int)
    suspend fun getReminders(timestamp: Long): List<NotificationData>
    suspend fun getUserPreferences(): UserPreferences?
    fun observeUserPreferences(currentUserId: String): Flow<UserPreferences>
    suspend fun saveUserPreferences(userPreferences: UserPreferences)
}