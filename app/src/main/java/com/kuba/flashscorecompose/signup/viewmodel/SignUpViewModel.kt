package com.kuba.flashscorecompose.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.signup.model.SignUpError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessage
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.isValidEmail
import com.kuba.flashscorecompose.utils.isValidPassword
import com.kuba.flashscorecompose.utils.passwordMatches
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class SignUpViewModel(private val authenticationRepository: AuthenticationDataSource) :
    ViewModel() {

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

    fun togglePasswordVisibility() {
        viewModelState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleRepeatPasswordVisibility() {
        viewModelState.update { it.copy(isRepeatPasswordVisible = !it.isRepeatPasswordVisible) }
    }

    fun onSignUpClick(openWelcomeScreen: () -> Unit) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                SnackbarManager.showMessage(R.string.email_error)
                viewModelState.update {
                    it.copy(
                        error = SignUpError.InvalidEmail(R.string.email_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            if (!password.isValidPassword()) {
                SnackbarManager.showMessage(R.string.password_error)
                viewModelState.update {
                    it.copy(
                        error = SignUpError.InvalidPassword(R.string.password_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            if (!password.passwordMatches(uiState.value.repeatPassword)) {
                SnackbarManager.showMessage(R.string.password_match_error)
                viewModelState.update {
                    it.copy(
                        error = SignUpError.NotMatchesPassword(R.string.password_match_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            viewModelState.update {
                when (val result =
                    authenticationRepository.signUpWithEmailAndPassword(email, password)) {
                    is RepositoryResult.Success -> {
                        openWelcomeScreen()
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        val message = result.error.statusMessage?.let { statusMessage ->
                            SnackbarMessage.StringSnackbar(statusMessage)
                        } ?: SnackbarMessage.ResourceSnackbar(R.string.generic_error)
                        SnackbarManager.showMessage(message)
                        it.copy(
                            error = SignUpError.AuthenticationError(
                                result.error,
                                R.string.generic_error
                            ),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}