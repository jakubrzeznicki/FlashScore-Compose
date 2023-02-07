package com.kuba.flashscorecompose.signin.viewmodel

import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.account.service.AccountService
import com.kuba.flashscorecompose.account.service.LogService
import com.kuba.flashscorecompose.main.viewmodel.FlashScoreViewModel
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.utils.isValidEmail
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class SignInViewModel(private val accountService: AccountService, logService: LogService) :
    FlashScoreViewModel(logService) {

    private val viewModelState = MutableStateFlow((SignInViewModelState()))
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())
    private val email
        get() = viewModelState.value.email
    private val password
        get() = viewModelState.value.password

    fun onEmailChange(newValue: String) {
        viewModelState.update {
            it.copy(email = newValue)
        }
    }

    fun onPasswordChange(newValue: String) {
        viewModelState.update { it.copy(password = newValue) }
    }

    fun onSignInClick(openHomeScreen: () -> Unit) {
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                SnackbarManager.showMessage(R.string.email)
                return@launch
            }
            if (password.isBlank()) {
                SnackbarManager.showMessage(R.string.password)
                return@launch
            }
            launchCatching {
                accountService.authenticate(email, password)
                openHomeScreen()
            }
        }
    }

    fun onForgotPasswordClick() {
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                SnackbarManager.showMessage(R.string.email_error)
                return@launch
            }
            accountService.sendRecoveryEmail(email)
            SnackbarManager.showMessage(R.string.recovery_email_sent)
        }
    }
}