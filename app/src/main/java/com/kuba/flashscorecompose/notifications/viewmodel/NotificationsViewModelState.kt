package com.kuba.flashscorecompose.notifications.viewmodel

import com.kuba.flashscorecompose.notifications.model.NotificationData
import com.kuba.flashscorecompose.notifications.model.NotificationsUiState

/**
 * Created by jrzeznicki on 06/03/2023.
 */
data class NotificationsViewModelState(val notifications: List<NotificationData> = emptyList()) {
    fun toUiState(): NotificationsUiState = if (notifications.isEmpty()) {
        NotificationsUiState.NoData
    } else {
        NotificationsUiState.HasData(notifications = notifications)
    }
}