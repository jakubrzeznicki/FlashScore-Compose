package com.example.signin.signin.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.repository.AuthenticationDataSource
import com.example.common.utils.RepositoryResult
import com.example.common.utils.isValidEmail
import com.example.common.utils.md5
import com.example.data.user.repository.UserDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.model.user.User
import com.example.signin.signin.model.SignInError
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import  com.example.ui.R as uiR

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class SignInViewModel(
    private val authenticationRepository: AuthenticationDataSource,
    private val userRepository: UserDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow((SignInViewModelState()))
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

    fun togglePasswordVisibility() {
        viewModelState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onSignInClick(openHomeScreen: () -> Unit, openOnBoardingScreen: () -> Unit) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                snackbarManager.showMessage(
                    com.example.ui.R.string.email_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = SignInError.InvalidEmail(com.example.ui.R.string.email_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            if (password.isBlank()) {
                snackbarManager.showMessage(
                    com.example.ui.R.string.empty_password_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = SignInError.BlankPassword(com.example.ui.R.string.empty_password_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            viewModelState.update {
                when (
                    val result =
                        authenticationRepository.signInWithEmailAndPassword(email, password)
                ) {
                    is RepositoryResult.Success -> {
                        val currentUserId = authenticationRepository.currentUserId
                        val isKeepLogged = viewModelState.value.keepLogged
                        val currentUser = authenticationRepository.currentUser
                        val savedUser = userRepository.getUserById(currentUserId)
                        val user = User(
                            id = currentUserId,
                            name = currentUser?.displayName.orEmpty(),
                            email = email,
                            password = password.md5(),
                            phone = savedUser?.phone ?: currentUser?.phoneNumber.orEmpty(),
                            address = savedUser?.address.orEmpty(),
                            photoUri = savedUser?.photoUri ?: Uri.EMPTY,
                            isAnonymous = currentUser?.isAnonymous ?: false
                        )
                        snackbarManager.showMessage(
                            uiR.string.successfully_signed_in,
                            SnackbarMessageType.Success
                        )
                        userRepository.saveUser(user)
                        userPreferencesRepository.saveCurrentUserId(currentUserId, isKeepLogged)
                        if (userPreferencesRepository.getIsOnBoardingCompleted() == true) {
                            openHomeScreen()
                        } else {
                            openOnBoardingScreen()
                        }
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            error = SignInError.AuthenticationError(result.error),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onForgotPasswordClick() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                snackbarManager.showMessage(
                    com.example.ui.R.string.email_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = SignInError.InvalidEmail(com.example.ui.R.string.email_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            viewModelState.update {
                when (val result = authenticationRepository.sendRecoveryEmail(email)) {
                    is RepositoryResult.Success -> {
                        snackbarManager.showMessage(
                            com.example.ui.R.string.recovery_email_sent,
                            SnackbarMessageType.Success
                        )
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            error = SignInError.AuthenticationError(result.error),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}
