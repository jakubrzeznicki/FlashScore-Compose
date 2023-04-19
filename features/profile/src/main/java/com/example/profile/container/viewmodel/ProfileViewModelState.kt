package com.example.profile.container.viewmodel

import com.example.model.user.User
import com.example.profile.container.model.ProfileError
import com.example.profile.container.model.ProfileUiState

/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class ProfileViewModelState(
    val isLoading: Boolean = false,
    val error: ProfileError = ProfileError.NoError,
    val user: User? = User()
) {
    fun toUiState(): ProfileUiState = if (user != null) {
        ProfileUiState.HasData(isLoading, error, user)
    } else {
        ProfileUiState.NoData(isLoading, error)
    }
}
