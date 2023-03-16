package com.example.notifications.model

import com.example.model.notificationdata.NotificationData

/**
 * Created by jrzeznicki on 06/03/2023.
 */
sealed interface NotificationsUiState {
    class HasData(val notifications: List<NotificationData>) : NotificationsUiState
    object NoData : NotificationsUiState
}
