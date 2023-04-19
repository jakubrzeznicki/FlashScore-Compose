package com.example.profile.details.viewmodel

import com.example.model.user.User
import com.example.profile.container.model.ProfileError
import com.example.profile.details.model.ProfileDetailsUiState

/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class ProfileDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: ProfileError = ProfileError.NoError,
    val user: User? = User(),
    val isNameExpanded: Boolean = false,
    val isEmailExpanded: Boolean = false,
    val isPhoneExpanded: Boolean = false,
    val isAddressExpanded: Boolean = false
) {
    fun toUiState(): ProfileDetailsUiState = if (user != null) {
        ProfileDetailsUiState.HasData(
            isLoading,
            error,
            isNameExpanded,
            isEmailExpanded,
            isPhoneExpanded,
            isAddressExpanded,
            user
        )
    } else {
        ProfileDetailsUiState.NoData(
            isLoading,
            error,
            isNameExpanded,
            isEmailExpanded,
            isPhoneExpanded,
            isAddressExpanded
        )
    }
}
