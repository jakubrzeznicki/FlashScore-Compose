package com.example.notifications.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.notification.repository.NotificationsDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.model.notificationdata.NotificationData
import com.example.notificationservice.manager.ReminderManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 06/03/2023.
 */
class NotificationsViewModel(
    private val notificationsRepository: NotificationsDataSource,
    private val reminderManagr: ReminderManager,
    private val userPreferencesRepository: UserPreferencesDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(NotificationsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observeNotifications()
    }

    fun cancelNotification(notificationData: NotificationData) {
        viewModelScope.launch {
            notificationsRepository.deleteReminder(notificationData.id, notificationData.userId)
            reminderManagr.cancelReminder(notificationData)
        }
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            notificationsRepository.observeActiveReminders(timestamp, currentUserId)
                .collect { notifications ->
                    viewModelState.update { it.copy(notifications = notifications) }
                }
        }
    }
}
