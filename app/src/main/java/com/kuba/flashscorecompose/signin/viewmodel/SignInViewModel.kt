package com.kuba.flashscorecompose.signin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.signin.model.SignInError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessage
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.isValidEmail
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class SignInViewModel(
    private val authenticationRepository: AuthenticationDataSource,
    private val userRepository: UserDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow((SignInViewModelState()))
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())
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
                SnackbarManager.showMessage(R.string.email_error)
                viewModelState.update {
                    it.copy(
                        error = SignInError.InvalidEmail(R.string.email_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            if (password.isBlank()) {
                SnackbarManager.showMessage(R.string.empty_password_error)
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
                        val user = User(
                            id = currentUserId,
                            name = currentUser?.displayName.orEmpty(),
                            email = email,
                            password = password,
                            phone = currentUser?.phoneNumber.orEmpty(),
                            isAnonymous = currentUser?.isAnonymous ?: false
                        )
                        userRepository.saveCurrentUserId(currentUserId, isKeepLogged)
                        userRepository.saveUser(user)
                        if (userRepository.getIsOnBoardingCompleted()) openHomeScreen() else openOnBoardingScreen()
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        val message = result.error.statusMessage?.let { statusMessage ->
                            SnackbarMessage.StringSnackbar(statusMessage)
                        } ?: SnackbarMessage.ResourceSnackbar(R.string.generic_error)
                        SnackbarManager.showMessage(message)
                        it.copy(
                            error = SignInError.AuthenticationError(
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

    fun onForgotPasswordClick() {
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                SnackbarManager.showMessage(R.string.email_error)
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
                        SnackbarManager.showMessage(R.string.recovery_email_sent)
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        val message = result.error.statusMessage?.let { statusMessage ->
                            SnackbarMessage.StringSnackbar(statusMessage)
                        } ?: SnackbarMessage.ResourceSnackbar(R.string.generic_error)
                        SnackbarManager.showMessage(message)
                        it.copy(
                            error = SignInError.AuthenticationError(
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