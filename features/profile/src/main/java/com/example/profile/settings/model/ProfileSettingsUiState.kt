package com.example.profile.settings.model

import com.example.model.user.User

/**
 * Created by jrzeznicki on 09/02/2023.
 */
data class ProfileSettingsUiState(
    val isLoading: Boolean,
    val user: User,
    val error: ProfileSettingsError,
    val password: String,
    val repeatPassword: String,
    val isPasswordCardExpanded: Boolean,
    val isPasswordVisible: Boolean,
    val isRepeatPasswordVisible: Boolean,
    val shouldShowConfirmDeleteAccountDialog: Boolean,
    val shouldShowConfirmSignOutDialog: Boolean
)
