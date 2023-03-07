package com.kuba.flashscorecompose.notifications

import com.kuba.flashscorecompose.notifications.model.NotificationData

/**
 * Created by jrzeznicki on 03/03/2023.
 */
interface ReminderManager {
    fun startReminder(notificationData: NotificationData)
    fun cancelReminder(notificationData: NotificationData)
}