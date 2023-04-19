package com.example.profile.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.repository.AuthenticationDataSource
import com.example.common.utils.RepositoryResult
import com.example.common.utils.isValidPassword
import com.example.common.utils.md5
import com.example.common.utils.passwordMatches
import com.example.data.user.repository.UserDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.profile.R
import com.example.profile.settings.model.ProfileSettingsError
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessage
import com.example.ui.snackbar.SnackbarMessageType
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
                snackbarManager.showMessage(
                    com.example.ui.R.string.password_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(
                        error = ProfileSettingsError.InvalidPassword,
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
                        error = ProfileSettingsError.NotMatchesPassword,
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
                        viewModelState.value.user?.copy(password = password.md5())?.let { user ->
                            userRepository.saveUser(user)
                        }
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            error = ProfileSettingsError.AuthenticationError(result.error),
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
                            error = ProfileSettingsError.AuthenticationError(result.error),
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
                        userRepository.deleteUser(userId)
                        snackbarManager.showMessage(
                            R.string.account_deleted,
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
                            error = ProfileSettingsError.AuthenticationError(result.error),
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
                if (user == null) {
                    viewModelState.update { it.copy(error = ProfileSettingsError.EmptyProfile) }
                    return@collect
                }
                viewModelState.update { it.copy(user = user) }
            }
        }
    }
}