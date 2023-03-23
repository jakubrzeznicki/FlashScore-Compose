package com.example.welcome.welcome.viewmodel

import com.example.welcome.welcome.model.WelcomeUiState

/**
 * Created by jrzeznicki on 06/02/2023.
 */
data class WelcomeViewModelState(val isLoading: Boolean = false) {
    fun toUiState(): WelcomeUiState = WelcomeUiState(isLoading)
}
