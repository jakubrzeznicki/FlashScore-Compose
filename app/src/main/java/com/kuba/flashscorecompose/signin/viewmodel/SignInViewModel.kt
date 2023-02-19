package com.kuba.flashscorecompose.signin.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.signin.model.SignInError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager.showSnackbarMessage
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.isValidEmail
import com.kuba.flashscorecompose.utils.md5
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class SignInViewModel(
    private val authenticationRepository: AuthenticationDataSource,
    private val userRepository: UserDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource
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

    fun toggleKeepLogged() {
        viewModelState.update { it.copy(keepLogged = !it.keepLogged) }
    }

    fun onSignInClick(openHomeScreen: () -> Unit, openOnBoardingScreen: () -> Unit) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                SnackbarManager.showMessage(R.string.email_error, SnackbarMessageType.Error)
                viewModelState.update {
                    it.copy(
                        error = SignInError.InvalidEmail(R.string.email_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            if (password.isBlank()) {
                SnackbarManager.showMessage(
                    R.string.empty_password_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = SignInError.BlankPassword(R.string.empty_password_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            viewModelState.update {
                when (val result =
                    authenticationRepository.signInWithEmailAndPassword(email, password)) {
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
                        SnackbarManager.showMessage(
                            R.string.successfully_signed_in,
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
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
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
                SnackbarManager.showMessage(R.string.email_error, SnackbarMessageType.Error)
                viewModelState.update {
                    it.copy(
                        error = SignInError.InvalidEmail(R.string.email_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            viewModelState.update {
                when (val result = authenticationRepository.sendRecoveryEmail(email)) {
                    is RepositoryResult.Success -> {
                        SnackbarManager.showMessage(
                            R.string.recovery_email_sent,
                            SnackbarMessageType.Success
                        )
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
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