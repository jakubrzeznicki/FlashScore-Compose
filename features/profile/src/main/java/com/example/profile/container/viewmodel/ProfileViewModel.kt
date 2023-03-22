package com.example.profile.container.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.repository.AuthenticationDataSource
import com.example.common.utils.RepositoryResult
import com.example.data.user.repository.UserDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.profile.R
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class ProfileViewModel(
    private val userRepository: UserDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val authenticationRepository: AuthenticationDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    private val viewModelState = MutableStateFlow(ProfileViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            if (currentUserId.isEmpty()) authenticationRepository.signOut()
            userRepository.observeUser(currentUserId).collect { user ->
                viewModelState.update { it.copy(user = user) }
            }
        }
    }

    fun updatePhoto(photoUri: Uri) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            viewModelState.update {
                when (val result = authenticationRepository.uploadPhoto(photoUri = photoUri)) {
                    is RepositoryResult.Success -> {
                        snackbarManager.showMessage(
                            R.string.successfully_updated_photo,
                            SnackbarMessageType.Success
                        )
                        userRepository.saveUser(
                            viewModelState.value.user.copy(photoUri = result.data ?: Uri.EMPTY)
                        )
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }
}
