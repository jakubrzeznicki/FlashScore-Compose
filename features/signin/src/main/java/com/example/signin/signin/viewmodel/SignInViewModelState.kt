package com.example.signin.signin.viewmodel

import com.example.signin.signin.model.SignInError
import com.example.signin.signin.model.SignInUiState

/**
 * Created by jrzeznicki on 05/02/2023.
 */
data class SignInViewModelState(
    val isLoading: Boolean = false,
    val error: SignInError = SignInError.NoError,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val keepLogged: Boolean = false
) {
    fun toUiState(): SignInUiState {
        return SignInUiState(isLoading, error, email, password, isPasswordVisible, keepLogged)
    }
}
