package com.kuba.flashscorecompose.welcome.viewmodel

import com.kuba.flashscorecompose.welcome.model.WelcomeUiState

/**
 * Created by jrzeznicki on 06/02/2023.
 */
data class WelcomeViewModelState(val isLoading: Boolean = false) {
    fun toUiState(): WelcomeUiState = WelcomeUiState(isLoading)
}
