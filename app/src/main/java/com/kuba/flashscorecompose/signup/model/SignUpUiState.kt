package com.kuba.flashscorecompose.signup.model

/**
 * Created by jrzeznicki on 05/02/2023.
 */
data class SignUpUiState(
    val isLoading: Boolean,
    val error: SignUpError,
    val email: String,
    val password: String,
    val repeatPassword: String,
    val isPasswordVisible: Boolean,
    val isRepeatPasswordVisible: Boolean
)