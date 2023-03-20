package com.kuba.flashscorecompose.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager.showSnackbarMessage
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class WelcomeViewModel(
    private val authenticationRepository: AuthenticationDataSource,
    private val userRepository: UserDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource
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
                        SnackbarManager.showMessage(
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
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }
}