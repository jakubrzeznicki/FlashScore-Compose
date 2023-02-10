package com.kuba.flashscorecompose.profile.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.profile.settings.model.ProfileSettingsItem
import com.kuba.flashscorecompose.signup.model.SignUpError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessage
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.isValidPassword
import com.kuba.flashscorecompose.utils.passwordMatches
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 09/02/2023.
 */
class ProfileSettingsViewModel(
    private val userId: String,
    private val userRepository: UserDataSource,
    private val authenticationRepository: AuthenticationDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(ProfileSettingsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())
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

    fun onCardClick(profileSettingsItem: ProfileSettingsItem) {
        viewModelState.update {
            when (profileSettingsItem) {
                is ProfileSettingsItem.Password ->
                    it.copy(isPasswordCardExpanded = !it.isPasswordCardExpanded)
                else -> {
                    it.copy()
                }
            }
        }
    }

    fun onSavePasswordClick() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
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
                when (val result = authenticationRepository.updatePassword(password)) {
                    is RepositoryResult.Success -> {
                        SnackbarMessage.ResourceSnackbar(R.string.successfully_updated_password)
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        val message = result.error.statusMessage?.let { statusMessage ->
                            SnackbarMessage.StringSnackbar(statusMessage)
                        } ?: SnackbarMessage.ResourceSnackbar(R.string.error_updated_password)
                        SnackbarManager.showMessage(message)
                        it.copy(
                            error = SignUpError.AuthenticationError(
                                result.error,
                                R.string.error_updated_password
                            ),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onSignOutClick(restartApp: () -> Unit) {
        viewModelScope.launch {
            when (val result = authenticationRepository.signOut()) {
                is RepositoryResult.Success -> {
                    SnackbarManager.showMessage(R.string.successfully_signed_out)
                    restartApp()
                }
                is RepositoryResult.Error -> {
                    val message = result.error.statusMessage?.let { statusMessage ->
                        SnackbarMessage.StringSnackbar(statusMessage)
                    } ?: SnackbarMessage.ResourceSnackbar(R.string.error_signed_out)
                    SnackbarManager.showMessage(message)
                }
            }
        }
    }

    fun onDeleteAccountClick(restartApp: () -> Unit) {
        viewModelScope.launch {
            when (val result = authenticationRepository.deleteAccount()) {
                is RepositoryResult.Success -> {
                    SnackbarManager.showMessage(R.string.account_deleted)
                    userRepository.deleteUser(userId)
                    restartApp()
                }
                is RepositoryResult.Error -> {
                    val message = result.error.statusMessage?.let { statusMessage ->
                        SnackbarMessage.StringSnackbar(statusMessage)
                    } ?: SnackbarMessage.ResourceSnackbar(R.string.error_account_deleted)
                    SnackbarManager.showMessage(message)
                }
            }
        }
    }

    private fun observeUser() {
        viewModelScope.launch {
            val currentUserId = userRepository.getCurrentUserId()
            userRepository.observeUser(currentUserId).collect { user ->
                viewModelState.update { it.copy(user = user) }
            }
        }
    }
}