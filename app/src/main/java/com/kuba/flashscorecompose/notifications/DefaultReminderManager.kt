package com.kuba.flashscorecompose.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.annotation.RequiresApi
import com.kuba.flashscorecompose.notifications.model.NotificationData
import com.kuba.flashscorecompose.utils.getPendingIntentFlag
import java.util.*

/**
 * Created by jrzeznicki on 03/03/2023.
 */
class DefaultReminderManager(val context: Context) : ReminderManager {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun startReminder(notificationData: NotificationData) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
        if (hasPermission) {
            val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
            intent.putExtra(NOTIFICATION_DATA_KEY, notificationData)
            val pendingIntent = PendingIntent.getBroadcast(
                context.applicationContext,
                notificationData.id,
                intent,
                getPendingIntentFlag()
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notificationData.timestamp,
                pendingIntent
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val settingsIntent = Intent().apply {
                    action = ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(settingsIntent)
            }
        }
    }

    override fun cancelReminder(notificationData: NotificationData) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.putExtra(NOTIFICATION_DATA_KEY, notificationData)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            notificationData.id,
            intent,
            getPendingIntentFlag()
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val NOTIFICATION_DATA_KEY = "NOTIFICATION_DATA_KEY"
    }
}
