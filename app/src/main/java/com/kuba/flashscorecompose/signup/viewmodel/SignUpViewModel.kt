package com.kuba.flashscorecompose.signup.viewmodel

import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.account.service.AccountService
import com.kuba.flashscorecompose.account.service.LogService
import com.kuba.flashscorecompose.main.viewmodel.FlashScoreViewModel
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.utils.isValidEmail
import com.kuba.flashscorecompose.utils.isValidPassword
import com.kuba.flashscorecompose.utils.passwordMatches
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class SignUpViewModel(private val accountService: AccountService, logService: LogService) :
    FlashScoreViewModel(logService) {

    private val viewModelState = MutableStateFlow((SignUpViewModelState()))
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

    fun onRepeatPasswordChange(newValue: String) {
        viewModelState.update { it.copy(repeatPassword = newValue) }
    }

    fun onSignUpClick(openWelcomeScreen: () -> Unit) {
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                SnackbarManager.showMessage(R.string.email_error)
                return@launch
            }
            if (!password.isValidPassword()) {
                SnackbarManager.showMessage(R.string.password_error)
                return@launch
            }
            if (!password.passwordMatches(uiState.value.repeatPassword)) {
                SnackbarManager.showMessage(R.string.password_match_error)
                return@launch
            }
            launchCatching {
                accountService.signUpWithEmailAndPassword(email, password)
                openWelcomeScreen()
            }
        }
    }
}