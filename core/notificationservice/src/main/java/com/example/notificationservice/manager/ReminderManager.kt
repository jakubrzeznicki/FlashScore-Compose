package com.example.notificationservice.manager

import com.example.model.notificationdata.NotificationData

/**
 * Created by jrzeznicki on 03/03/2023.
 */
interface ReminderManager {
    fun startReminder(notificationData: NotificationData)
    fun cancelReminder(notificationData: NotificationData)
}
