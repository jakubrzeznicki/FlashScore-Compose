package com.kuba.flashscorecompose.notifications.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by jrzeznicki on 21/02/2023.
 */
@Parcelize
data class NotificationData(
    val id: Int,
    val round: String,
    val formattedDate: String,
    val homeTeam: String,
    val awayTeam: String,
    val timestamp: Long
) : Parcelable
