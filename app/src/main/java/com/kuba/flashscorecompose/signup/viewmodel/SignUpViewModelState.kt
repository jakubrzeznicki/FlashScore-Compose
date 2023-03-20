package com.kuba.flashscorecompose.signup.viewmodel

import com.kuba.flashscorecompose.signup.model.SignUpError
import com.kuba.flashscorecompose.signup.model.SignUpUiState

/**
 * Created by jrzeznicki on 05/02/2023.
 */
data class SignUpViewModelState(
    val isLoading: Boolean = false,
    val error: SignUpError = SignUpError.NoError,
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isRepeatPasswordVisible: Boolean = false
) {
    fun toUiState(): SignUpUiState {
        return SignUpUiState(
            isLoading,
            error,
            email,
            password,
            repeatPassword,
            isPasswordVisible,
            isRepeatPasswordVisible
        )
    }
}
