package com.kuba.flashscorecompose.notifications

import android.app.NotificationManager
import com.kuba.flashscorecompose.notifications.model.NotificationData

/**
 * Created by jrzeznicki on 03/03/2023.
 */
interface FixtureNotification {
    fun sendFixtureNotification(
        notificationManager: NotificationManager,
        notificationData: NotificationData
    )
}
