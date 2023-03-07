package com.kuba.flashscorecompose.data.notifications.mapper

import com.kuba.flashscorecompose.data.notifications.local.model.NotificationDataEntity
import com.kuba.flashscorecompose.notifications.model.NotificationData

/**
 * Created by jrzeznicki on 06/03/2023.
 */
fun NotificationData.toNotificationDataEntity(): NotificationDataEntity {
    return NotificationDataEntity(
        id = id,
        round = round,
        formattedDate = formattedDate,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        timestamp = timestamp
    )
}

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