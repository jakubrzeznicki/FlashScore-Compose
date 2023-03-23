package com.example.welcome.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.repository.AuthenticationDataSource
import com.example.common.utils.RepositoryResult
import com.example.data.user.repository.UserDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.model.user.User
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import com.example.welcome.R
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class WelcomeViewModel(
    private val authenticationRepository: AuthenticationDataSource,
    private val userRepository: UserDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(WelcomeViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun createAnonymousAccount(openHomeScreen: () -> Unit, openOnBoardingScreen: () -> Unit) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            viewModelState.update {
                when (val result = authenticationRepository.createAnonymousAccount()) {
                    is RepositoryResult.Success -> {
                        val currentUserId = authenticationRepository.currentUserId
                        val currentUser = authenticationRepository.currentUser
                        val user = User(
                            id = currentUserId,
                            name = currentUser?.displayName.orEmpty(),
                            email = currentUser?.email.orEmpty(),
                            phone = currentUser?.phoneNumber.orEmpty(),
                            isAnonymous = currentUser?.isAnonymous ?: false
                        )
                        userRepository.saveUser(user)
                        userPreferencesRepository.saveCurrentUserId(
                            currentUserId,
                            isKeepLogged = false
                        )
                        snackbarManager.showMessage(
                            R.string.logged_as_guest,
                            SnackbarMessageType.Success
                        )
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
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }
}
