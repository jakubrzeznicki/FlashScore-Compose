package com.kuba.flashscorecompose.signin.model

/**
 * Created by jrzeznicki on 05/02/2023.
 */
data class SignInUiState(
    val isLoading: Boolean,
    val error: SignInError,
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val keepLogged: Boolean
)
