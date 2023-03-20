package com.kuba.flashscorecompose.data.notifications.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.notifications.local.model.NotificationDataEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 06/03/2023.
 */
class NotificationsLocal(private val roomStorage: RoomStorage) : NotificationsLocalDataSource {
    override suspend fun saveReminder(notificationData: NotificationDataEntity) {
        roomStorage.getDatabase().notificationsDao().saveReminder(notificationData)
    }

    override suspend fun deleteReminder(id: Int) {
        roomStorage.getDatabase().notificationsDao().deleteReminder(id)
    }

    override suspend fun getActiveReminders(timestamp: Long): List<NotificationDataEntity> {
        return roomStorage.getDatabase().notificationsDao().getReminders(timestamp)
    }

    override fun observeActiveReminders(timestamp: Long): Flow<List<NotificationDataEntity>> {
        return roomStorage.getDatabase().notificationsDao().observeReminders(timestamp)
    }
}
