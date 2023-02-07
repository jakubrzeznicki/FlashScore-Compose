package com.kuba.flashscorecompose.signup.model

/**
 * Created by jrzeznicki on 05/02/2023.
 */
data class SignUpUiState(
    val email: String,
    val password: String,
    val repeatPassword: String
)