package com.kuba.flashscorecompose.welcome.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.kuba.flashscorecompose.account.service.AccountService
import com.kuba.flashscorecompose.account.service.LogService
import com.kuba.flashscorecompose.main.viewmodel.FlashScoreViewModel
import com.kuba.flashscorecompose.welcome.model.WelcomeError
import kotlinx.coroutines.flow.*

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class WelcomeViewModel(private val accountService: AccountService, logService: LogService) :
    FlashScoreViewModel(logService) {

    private val viewModelState = MutableStateFlow(WelcomeViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun createAnonymousAccount(openHomeScreen: () -> Unit) {
        launchCatching(snackbar = false) {
            try {
                accountService.createAnonymousAccount()
            } catch (e: FirebaseAuthException) {
                viewModelState.update { it.copy(error = WelcomeError.AuthenticationError(e.message.orEmpty())) } // dprobic generix message
                throw e
            }
            openHomeScreen()
        }
    }
}