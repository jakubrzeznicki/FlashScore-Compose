package com.example.data.notification.repository

import com.example.data.notification.local.NotificationsLocalDataSource
import com.example.data.notification.mapper.toNotificationDataEntity
import com.example.database.notifications.mapper.toNotificationData
import com.example.model.notificationdata.NotificationData
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
