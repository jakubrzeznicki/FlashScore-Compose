package com.kuba.flashscorecompose.profile.container.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessage
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class ProfileViewModel(
    private val userRepository: UserDataSource,
    private val authenticationRepository: AuthenticationDataSource
) : ViewModel() {
    private val viewModelState = MutableStateFlow(ProfileViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            val currentUserId = userRepository.getCurrentUserId()
            userRepository.observeUser(currentUserId).collect { user ->
                viewModelState.update { it.copy(user = user) }
            }
        }
    }

    private fun updatePhoto(photoUri: Uri?) {
        viewModelScope.launch {
            when (val result = authenticationRepository.updatePhotoUrl(photoUri = photoUri)) {
                is RepositoryResult.Success -> {
                    SnackbarManager.showMessage(R.string.successfully_updated_photo)
                    userRepository.saveUser(viewModelState.value.user)
                }
                is RepositoryResult.Error -> {
                    val message = result.error.statusMessage?.let { statusMessage ->
                        SnackbarMessage.StringSnackbar(statusMessage)
                    } ?: SnackbarMessage.ResourceSnackbar(R.string.error_updated_photo)
                    SnackbarManager.showMessage(message)
                }
            }
        }
    }
}