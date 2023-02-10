package com.kuba.flashscorecompose.profile.settings.viewmodel

import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.profile.settings.model.ProfileSettingsUiState
import com.kuba.flashscorecompose.signup.model.SignUpError

/**
 * Created by jrzeznicki on 09/02/2023.
 */
data class ProfileSettingsViewModelState(
    val isLoading: Boolean = false,
    val user: User = User(),
    val error: SignUpError = SignUpError.NoError,
    val password: String = "",
    val repeatPassword: String = "",
    val isPasswordCardExpanded: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isRepeatPasswordVisible: Boolean = false,
    val shouldShowConfirmDeleteAccountDialog: Boolean = false,
    val shouldShowConfirmSignOutDialog: Boolean = false
) {
    fun toUiState(): ProfileSettingsUiState {
        return ProfileSettingsUiState(
            isLoading = isLoading,
            user = user,
            error = error,
            password = password,
            repeatPassword = repeatPassword,
            isPasswordCardExpanded = isPasswordCardExpanded,
            isPasswordVisible = isPasswordVisible,
            isRepeatPasswordVisible = isRepeatPasswordVisible,
            shouldShowConfirmDeleteAccountDialog = shouldShowConfirmDeleteAccountDialog,
            shouldShowConfirmSignOutDialog = shouldShowConfirmSignOutDialog
        )
    }
}