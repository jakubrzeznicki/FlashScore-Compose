package com.kuba.flashscorecompose.data.notifications

import com.kuba.flashscorecompose.data.notifications.local.NotificationsLocalDataSource
import com.kuba.flashscorecompose.data.notifications.mapper.toNotificationData
import com.kuba.flashscorecompose.data.notifications.mapper.toNotificationDataEntity
import com.kuba.flashscorecompose.notifications.model.NotificationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by jrzeznicki on 06/03/2023.
 */
class NotificationsRepository(private val local: NotificationsLocalDataSource) :
    NotificationsDataSource {
    override suspend fun saveReminder(notificationData: NotificationData) {
        local.saveReminder(notificationData.toNotificationDataEntity())
    }

    override suspend fun deleteReminder(id: Int) {
        local.deleteReminder(id)
    }

    override suspend fun getActiveReminders(timestamp: Long): List<NotificationData> {
        return local.getActiveReminders(timestamp).map { it.toNotificationData() }
    }

    override fun observeActiveReminders(timestamp: Long): Flow<List<NotificationData>> {
        return local.observeActiveReminders(timestamp)
            .map { it.map { notificationDataEntity -> notificationDataEntity.toNotificationData() } }
    }
}
