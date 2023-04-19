package com.example.data.notification.mapper

import com.example.database.notifications.model.NotificationDataEntity
import com.example.model.notificationdata.NotificationData

/**
 * Created by jrzeznicki on 14/03/2023.
 */
fun NotificationData.toNotificationDataEntity(): NotificationDataEntity {
    return NotificationDataEntity(
        id = id,
        round = round,
        formattedDate = formattedDate,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        timestamp = timestamp,
        userId = userId
    )
}
