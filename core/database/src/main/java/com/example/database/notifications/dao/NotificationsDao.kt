package com.example.database.notifications.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.notifications.model.NotificationDataEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 06/03/2023.
 */
@Dao
interface NotificationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(notificationData: NotificationDataEntity)

    @Query("DELETE FROM notification_data WHERE id = :id AND user_id = :currentUserId")
    suspend fun deleteReminder(id: Int, currentUserId: String)

    @Query("SELECT * FROM notification_data WHERE timestamp >= :currentTimestamp")
    suspend fun getReminders(currentTimestamp: Long): List<NotificationDataEntity>

    @Query("SELECT * FROM notification_data WHERE timestamp >= :currentTimestamp AND user_id = :currentUserId")
    fun observeReminders(
        currentTimestamp: Long,
        currentUserId: String
    ): Flow<List<NotificationDataEntity>>
}
