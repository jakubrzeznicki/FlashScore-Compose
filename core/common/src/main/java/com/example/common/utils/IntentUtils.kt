package com.example.common.utils

import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.os.Build

/**
 * Created by jrzeznicki on 06/03/2023.
 */
fun getPendingIntentFlag() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
} else {
    FLAG_UPDATE_CURRENT
}
