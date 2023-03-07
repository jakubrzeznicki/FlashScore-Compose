package com.kuba.flashscorecompose.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.main.view.MainActivity
import com.kuba.flashscorecompose.notifications.model.NotificationData
import com.kuba.flashscorecompose.utils.getPendingIntentFlag

/**
 * Created by jrzeznicki on 21/02/2023.
 */
class DefaultFixtureNotification(private val applicationContext: Context) : FixtureNotification {

    override fun sendFixtureNotification(
        notificationManager: NotificationManager,
        notificationData: NotificationData
    ) {
        notificationManager.createNotificationChannel()
        notificationManager.createNotification(
            getPendingIntent(notificationData.id),
            notificationData
        )
    }

    private fun getPendingIntent(id: Int): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "$MY_URI/$FIXTURE_ID_ARGS=$id".toUri(),
            applicationContext,
            MainActivity::class.java
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
                .setContentTitle(
                    "${applicationContext.getString(R.string.fixture)} " +
                            "${notificationData.homeTeam} ${applicationContext.getString(R.string.vs)} " +
                            notificationData.awayTeam
                )
                .setContentText(
                    "${applicationContext.getString(R.string.round)} " +
                            "${notificationData.round} \n${applicationContext.getString(R.string.start)} " +
                            notificationData.formattedDate
                )
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_explore)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setOngoing(false)
                .setShowWhen(false)
        notify(notificationData.id, notificationBuilder.build())
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "FIXTURE_NOTIFICATION_CHANNEL_ID"
        private const val NOTIFICATION_CHANNEL_NAME = "FIXTURE_NOTIFICATION_CHANNEL_NAME"
        const val MY_URI = "https://flashscorecompose.com"
        const val FIXTURE_ID_ARGS = "fixtureId"
    }
}