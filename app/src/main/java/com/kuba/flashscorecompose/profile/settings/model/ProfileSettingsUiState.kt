package com.kuba.flashscorecompose.profile.settings.model

import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.signup.model.SignUpError

/**
 * Created by jrzeznicki on 09/02/2023.
 */
data class ProfileSettingsUiState(
    val isLoading: Boolean,
    val user: User,
    val error: SignUpError,
    val password: String,
    val repeatPassword: String,
    val isPasswordCardExpanded: Boolean,
    val isPasswordVisible: Boolean,
    val isRepeatPasswordVisible: Boolean,
    val shouldShowConfirmDeleteAccountDialog: Boolean,
    val shouldShowConfirmSignOutDialog: Boolean
)