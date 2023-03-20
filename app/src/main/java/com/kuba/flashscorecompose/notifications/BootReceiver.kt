package com.kuba.flashscorecompose.notifications

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kuba.flashscorecompose.data.notifications.NotificationsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by jrzeznicki on 25/02/2023.
 */
class BootReceiver : BroadcastReceiver() {

    private val reminderManager by inject<ReminderManager>(ReminderManager::class.java)
    private val notificationsRepository by inject<NotificationsDataSource>(NotificationsDataSource::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                val currentTimestamp = System.currentTimeMillis()
                val reminders = notificationsRepository.getActiveReminders(currentTimestamp)
                reminders.forEach { notificationData ->
                    reminderManager.startReminder(notificationData)
                }
            }
        }
    }
}
