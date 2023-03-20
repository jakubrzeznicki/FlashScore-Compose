package com.kuba.flashscorecompose.data.notifications

import com.kuba.flashscorecompose.notifications.model.NotificationData
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 06/03/2023.
 */
interface NotificationsDataSource {
    suspend fun saveReminder(notificationData: NotificationData)
    suspend fun deleteReminder(id: Int)
    suspend fun getActiveReminders(timestamp: Long): List<NotificationData>
    fun observeActiveReminders(timestamp: Long): Flow<List<NotificationData>>
}
