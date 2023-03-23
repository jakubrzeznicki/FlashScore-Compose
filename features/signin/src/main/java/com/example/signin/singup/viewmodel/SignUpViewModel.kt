package com.example.signin.singup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.repository.AuthenticationDataSource
import com.example.common.utils.RepositoryResult
import com.example.common.utils.isValidEmail
import com.example.common.utils.isValidPassword
import com.example.common.utils.passwordMatches
import com.example.data.navigation.SignUpBackStackType
import com.example.signin.R
import com.example.signin.singup.model.SignUpError
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class SignUpViewModel(
    private val signUpBackStackType: SignUpBackStackType,
    private val authenticationRepository: AuthenticationDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow((SignUpViewModelState()))
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )
    private val email
        get() = viewModelState.value.email
    private val password
        get() = viewModelState.value.password

    fun onEmailChange(newValue: String) {
        viewModelState.update { it.copy(email = newValue) }
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
                snackbarManager.showMessage(
                    com.example.ui.R.string.email_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = SignUpError.InvalidEmail(com.example.ui.R.string.email_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            if (!password.isValidPassword()) {
                snackbarManager.showMessage(
                    com.example.ui.R.string.password_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = SignUpError.InvalidPassword(com.example.ui.R.string.password_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            if (!password.passwordMatches(uiState.value.repeatPassword)) {
                snackbarManager.showMessage(
                    com.example.ui.R.string.password_match_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = SignUpError.NotMatchesPassword(com.example.ui.R.string.password_match_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            viewModelState.update {
                val result = when (signUpBackStackType) {
                    SignUpBackStackType.New ->
                        authenticationRepository.signUpWithEmailAndPassword(email, password)
                    SignUpBackStackType.Anonymous ->
                        authenticationRepository.linkAccount(email, password)
                }
                when (result) {
                    is RepositoryResult.Success -> {
                        snackbarManager.showMessage(
                            R.string.successfully_signed_up,
                            SnackbarMessageType.Success
                        )
                        openWelcomeScreen()
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            error = SignUpError.AuthenticationError(result.error),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}
