package com.kuba.flashscorecompose.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.kuba.flashscorecompose.notifications.DefaultReminderManager.Companion.NOTIFICATION_DATA_KEY
import com.kuba.flashscorecompose.notifications.model.NotificationData
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by jrzeznicki on 21/02/2023.
 */
class AlarmReceiver : BroadcastReceiver() {

    private val notification by inject<FixtureNotification>(FixtureNotification::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        val notificationData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NOTIFICATION_DATA_KEY, NotificationData::class.java)
        } else {
            intent.getParcelableExtra(NOTIFICATION_DATA_KEY)
        }
        notificationData?.let {
            notification.sendFixtureNotification(notificationManager, it)
        }
    }
}