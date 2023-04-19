package com.example.data.notification.local

import com.example.database.notifications.model.NotificationDataEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 06/03/2023.
 */
interface NotificationsLocalDataSource {
    suspend fun saveReminder(notificationData: NotificationDataEntity)
    suspend fun deleteReminder(id: Int, currentUserId: String)
    suspend fun getActiveReminders(timestamp: Long): List<NotificationDataEntity>
    fun observeActiveReminders(
        timestamp: Long,
        currentUserId: String
    ): Flow<List<NotificationDataEntity>>
}
