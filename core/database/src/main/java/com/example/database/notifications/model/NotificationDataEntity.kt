package com.example.database.notifications.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Created by jrzeznicki on 06/03/2023.
 */
@Entity(tableName = "notification_data", primaryKeys = ["id", "user_id"])
data class NotificationDataEntity(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "round") val round: String,
    @ColumnInfo(name = "formatted_date") val formattedDate: String,
    @ColumnInfo(name = "home_team") val homeTeam: String,
    @ColumnInfo(name = "away_team") val awayTeam: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "user_id") val userId: String
)
