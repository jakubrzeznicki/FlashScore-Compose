package com.kuba.flashscorecompose.notifications.model

/**
 * Created by jrzeznicki on 06/03/2023.
 */
sealed interface NotificationsUiState {
    class HasData(val notifications: List<NotificationData>) : NotificationsUiState
    object NoData : NotificationsUiState
}
