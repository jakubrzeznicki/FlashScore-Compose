package com.example.notifications.viewmodel

import com.example.model.notificationdata.NotificationData
import com.example.notifications.model.NotificationsUiState


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
