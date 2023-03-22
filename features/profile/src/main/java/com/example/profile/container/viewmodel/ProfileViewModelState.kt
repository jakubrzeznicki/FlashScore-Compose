package com.example.profile.container.viewmodel

import com.example.model.user.User
import com.example.profile.container.model.ProfileUiState

/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class ProfileViewModelState(val isLoading: Boolean = false, val user: User = User()) {
    fun toUiState(): ProfileUiState = ProfileUiState(isLoading, user)
}
