package com.example.profile.settings.model

import com.example.model.user.User

/**
 * Created by jrzeznicki on 09/02/2023.
 */
interface ProfileSettingsUiState {
    val isLoading: Boolean
    val error: ProfileSettingsError
    val password: String
    val repeatPassword: String
    val isPasswordCardExpanded: Boolean
    val isPasswordVisible: Boolean
    val isRepeatPasswordVisible: Boolean
    val shouldShowConfirmDeleteAccountDialog: Boolean
    val shouldShowConfirmSignOutDialog: Boolean

    data class HasData(
        override val isLoading: Boolean,
        override val error: ProfileSettingsError,
        override val password: String,
        override val repeatPassword: String,
        override val isPasswordCardExpanded: Boolean,
        override val isPasswordVisible: Boolean,
        override val isRepeatPasswordVisible: Boolean,
        override val shouldShowConfirmDeleteAccountDialog: Boolean,
        override val shouldShowConfirmSignOutDialog: Boolean,
        val user: User
    ) : ProfileSettingsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: ProfileSettingsError,
        override val password: String,
        override val repeatPassword: String,
        override val isPasswordCardExpanded: Boolean,
        override val isPasswordVisible: Boolean,
        override val isRepeatPasswordVisible: Boolean,
        override val shouldShowConfirmDeleteAccountDialog: Boolean,
        override val shouldShowConfirmSignOutDialog: Boolean
    ) : ProfileSettingsUiState
}
