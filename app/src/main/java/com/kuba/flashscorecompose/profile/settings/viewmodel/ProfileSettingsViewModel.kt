package com.kuba.flashscorecompose.profile.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.signup.model.SignUpError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessage
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.isValidPassword
import com.kuba.flashscorecompose.utils.md5
import com.kuba.flashscorecompose.utils.passwordMatches
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 09/02/2023.
 */
class ProfileSettingsViewModel(
    private val userId: String,
    private val userRepository: UserDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val authenticationRepository: AuthenticationDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(ProfileSettingsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )
    private val password
        get() = viewModelState.value.password

    fun setup() {
        observeUser()
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

    fun showConfirmDeleteAccountDialog(showDialog: Boolean) {
        viewModelState.update { it.copy(shouldShowConfirmDeleteAccountDialog = showDialog) }
    }

    fun showConfirmSignOutDialog(showDialog: Boolean) {
        viewModelState.update { it.copy(shouldShowConfirmSignOutDialog = showDialog) }
    }

    fun onPasswordClick() {
        viewModelState.update {
            it.copy(isPasswordCardExpanded = !it.isPasswordCardExpanded)
        }
    }

    fun onSavePasswordClick() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (!password.isValidPassword()) {
                snackbarManager.showMessage(R.string.password_error, SnackbarMessageType.Error)
                viewModelState.update {
                    it.copy(
                        error = SignUpError.InvalidPassword(R.string.password_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            if (!password.passwordMatches(uiState.value.repeatPassword)) {
                snackbarManager.showMessage(
                    R.string.password_match_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = SignUpError.NotMatchesPassword(R.string.password_match_error),
                        isLoading = false
                    )
                }
                return@launch
            }
            viewModelState.update {
                when (val result = authenticationRepository.updatePassword(password)) {
                    is RepositoryResult.Success -> {
                        SnackbarMessage.ResourceSnackbar(
                            R.string.successfully_updated_password,
                            SnackbarMessageType.Success
                        )
                        userRepository.saveUser(
                            viewModelState.value.user.copy(password = password.md5())
                        )
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

    fun onSignOutClick(restartApp: () -> Unit) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            viewModelState.update {
                when (val result = authenticationRepository.signOut()) {
                    is RepositoryResult.Success -> {
                        snackbarManager.showMessage(
                            R.string.successfully_signed_out,
                            SnackbarMessageType.Success
                        )
                        restartApp()
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

    fun onDeleteAccountClick(restartApp: () -> Unit) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            viewModelState.update {
                when (val result = authenticationRepository.deleteAccount()) {
                    is RepositoryResult.Success -> {
                        snackbarManager.showMessage(
                            R.string.account_deleted,
                            SnackbarMessageType.Success
                        )
                        userRepository.deleteUser(userId)
                        restartApp()
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

    private fun observeUser() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            userRepository.observeUser(currentUserId).collect { user ->
                viewModelState.update { it.copy(user = user) }
            }
        }
    }
}