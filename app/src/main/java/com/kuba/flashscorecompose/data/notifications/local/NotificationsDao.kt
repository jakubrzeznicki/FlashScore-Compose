package com.kuba.flashscorecompose.data.notifications.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.notifications.local.model.NotificationDataEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 06/03/2023.
 */
@Dao
interface NotificationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(notificationData: NotificationDataEntity)

    @Query("DELETE FROM notification_data WHERE id = :id")
    suspend fun deleteReminder(id: Int)

    @Query("SELECT * FROM notification_data WHERE timestamp >= :currentTimestamp")
    suspend fun getReminders(currentTimestamp: Long): List<NotificationDataEntity>

    @Query("SELECT * FROM notification_data WHERE timestamp >= :currentTimestamp")
    fun observeReminders(currentTimestamp: Long): Flow<List<NotificationDataEntity>>
}
