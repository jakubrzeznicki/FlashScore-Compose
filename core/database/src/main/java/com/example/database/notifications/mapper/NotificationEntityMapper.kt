package com.example.database.notifications.mapper

import com.example.database.notifications.model.NotificationDataEntity
import com.example.model.notificationdata.NotificationData

/**
 * Created by jrzeznicki on 13/03/2023.
 */
fun NotificationDataEntity.toNotificationData(): NotificationData {
    return NotificationData(
        id = id,
        round = round,
        formattedDate = formattedDate,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        timestamp = timestamp
    )
}