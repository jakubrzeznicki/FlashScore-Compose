package com.kuba.flashscorecompose.data.notifications.local

import com.kuba.flashscorecompose.data.notifications.local.model.NotificationDataEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 06/03/2023.
 */
interface NotificationsLocalDataSource {
    suspend fun saveReminder(notificationData: NotificationDataEntity)
    suspend fun deleteReminder(id: Int)
    suspend fun getActiveReminders(timestamp: Long): List<NotificationDataEntity>
    fun observeActiveReminders(timestamp: Long): Flow<List<NotificationDataEntity>>
}