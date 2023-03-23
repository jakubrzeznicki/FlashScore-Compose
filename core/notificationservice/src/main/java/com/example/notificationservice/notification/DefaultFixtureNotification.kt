package com.example.notificationservice.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.common.utils.getPendingIntentFlag
import com.example.model.notificationdata.NotificationData
import com.example.notificationservice.R

/**
 * Created by jrzeznicki on 21/02/2023.
 */
class DefaultFixtureNotification(private val applicationContext: Context) : FixtureNotification {

    override fun sendFixtureNotification(
        notificationManager: NotificationManager,
        notificationData: NotificationData
    ) {
        notificationManager.createNotificationChannel()
        val pendingIntent = getPendingIntent(notificationData.id)
        notificationManager.createNotification(pendingIntent, notificationData)
    }

    private fun getPendingIntent(id: Int): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "$MY_URI/$FIXTURE_ID_ARGS=$id".toUri(),
            applicationContext,
            Class.forName(MAIN_ACTIVITY_PATH)
        )
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, getPendingIntentFlag())
        }
    }

    private fun NotificationManager.createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            createNotificationChannel(notificationChannel)
        }
    }

    private fun NotificationManager.createNotification(
        pendingIntent: PendingIntent,
        notificationData: NotificationData
    ) {
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getContentTitle(notificationData))
                .setContentText(getContentText(notificationData))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_explore)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setOngoing(false)
                .setShowWhen(false)
        notify(notificationData.id, notificationBuilder.build())
    }

    private fun getContentTitle(notificationData: NotificationData): CharSequence {
        return "${applicationContext.getString(R.string.fixture)} " +
                "${notificationData.homeTeam} ${applicationContext.getString(R.string.vs)} " +
                notificationData.awayTeam
    }

    private fun getContentText(notificationData: NotificationData): CharSequence {
        return "${applicationContext.getString(R.string.round)} " +
                "${notificationData.round} \n${applicationContext.getString(R.string.start)} " +
                notificationData.formattedDate
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "FIXTURE_NOTIFICATION_CHANNEL_ID"
        private const val NOTIFICATION_CHANNEL_NAME = "FIXTURE_NOTIFICATION_CHANNEL_NAME"
        const val MY_URI = "https://flashscorecompose.com"
        const val FIXTURE_ID_ARGS = "fixtureId"
        const val MAIN_ACTIVITY_PATH = "com.kuba.flashscorecompose.main.MainActivity"
    }
}
