package com.kuba.flashscorecompose.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessage
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.welcome.model.WelcomeError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class WelcomeViewModel(private val authenticationRepository: AuthenticationDataSource) :
    ViewModel() {

    private val viewModelState = MutableStateFlow(WelcomeViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun createAnonymousAccount(openHomeScreen: () -> Unit) {
        viewModelScope.launch {
            viewModelState.update {
                when (val result = authenticationRepository.createAnonymousAccount()) {
                    is RepositoryResult.Success -> {
                        SnackbarManager.showMessage(R.string.logged_as_guest) //dać iny kolor tego snackbara
                        openHomeScreen()
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        val message = result.error.statusMessage?.let { statusMessage ->
                            SnackbarMessage.StringSnackbar(statusMessage)
                        } ?: SnackbarMessage.ResourceSnackbar(R.string.generic_error)
                        SnackbarManager.showMessage(message)
                        it.copy(
                            error = WelcomeError.AuthenticationError(
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