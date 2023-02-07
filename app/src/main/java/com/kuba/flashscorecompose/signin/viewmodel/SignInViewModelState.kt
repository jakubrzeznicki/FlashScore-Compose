package com.kuba.flashscorecompose.signin.viewmodel

import com.kuba.flashscorecompose.signin.model.SignInUiState

/**
 * Created by jrzeznicki on 05/02/2023.
 */
data class SignInViewModelState(
    val email: String = "",
    val password: String = ""
) {
    fun toUiState(): SignInUiState {
        return SignInUiState(email, password)
    }
}