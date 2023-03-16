package com.example.notificationservice.notification

import android.app.NotificationManager
import com.example.model.notificationdata.NotificationData

/**
 * Created by jrzeznicki on 03/03/2023.
 */
interface FixtureNotification {
    fun sendFixtureNotification(
        notificationManager: NotificationManager,
        notificationData: NotificationData
    )
}
