package com.kuba.flashscorecompose.signup.viewmodel

import com.kuba.flashscorecompose.signup.model.SignUpUiState

/**
 * Created by jrzeznicki on 05/02/2023.
 */
data class SignUpViewModelState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
) {
    fun toUiState(): SignUpUiState {
        return SignUpUiState(email, password, repeatPassword)
    }
}