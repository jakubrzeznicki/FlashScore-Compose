package com.kuba.flashscorecompose.profile.container.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
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
        Log.d("TEST_LOG", "currentUserId - ${authenticationRepository.currentUserId}")
        Log.d("TEST_LOG", "hasUser - ${authenticationRepository.hasUser}")
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
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