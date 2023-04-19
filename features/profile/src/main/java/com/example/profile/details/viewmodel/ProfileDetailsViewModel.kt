package com.example.profile.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.repository.AuthenticationDataSource
import com.example.common.utils.RepositoryResult
import com.example.common.utils.isValidEmail
import com.example.data.user.repository.UserDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.model.player.ProfileItem
import com.example.profile.R
import com.example.profile.container.model.ProfileError
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class ProfileDetailsViewModel(
    private val userRepository: UserDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val authenticationRepository: AuthenticationDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(ProfileDetailsViewModelState())
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

    fun onItemClick(profileItem: ProfileItem) {
        viewModelState.update {
            when (profileItem) {
                is ProfileItem.Name -> it.copy(isNameExpanded = !it.isNameExpanded)
                is ProfileItem.Email -> it.copy(isEmailExpanded = !it.isEmailExpanded)
                is ProfileItem.Phone -> it.copy(isPhoneExpanded = !it.isPhoneExpanded)
                is ProfileItem.Address -> it.copy(isAddressExpanded = !it.isAddressExpanded)
            }
        }
    }

    fun onDoneClick(profileItem: ProfileItem) {
        when (profileItem) {
            is ProfileItem.Name -> updateName(profileItem.value)
            is ProfileItem.Email -> updateEmail(profileItem.value)
            is ProfileItem.Phone -> updatePhone()
            is ProfileItem.Address -> updateAddress()
        }
    }

    fun onValueChange(profileItem: ProfileItem) {
        viewModelState.update {
            when (profileItem) {
                is ProfileItem.Name -> it.copy(user = it.user?.copy(name = profileItem.value))
                is ProfileItem.Email -> it.copy(user = it.user?.copy(email = profileItem.value))
                is ProfileItem.Phone -> it.copy(user = it.user?.copy(phone = profileItem.value))
                is ProfileItem.Address -> it.copy(user = it.user?.copy(address = profileItem.value))
            }
        }
    }

    private fun updateName(name: String) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            viewModelState.update {
                when (val result = authenticationRepository.updateName(name = name)) {
                    is RepositoryResult.Success -> {
                        snackbarManager.showMessage(
                            R.string.successfully_updated_name,
                            SnackbarMessageType.Success
                        )
                        viewModelState.value.user?.let { user -> userRepository.saveUser(user) }
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

    private fun updateEmail(email: String) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (!email.isValidEmail()) {
                snackbarManager.showMessage(
                    com.example.ui.R.string.email_error,
                    SnackbarMessageType.Error
                )
                viewModelState.update {
                    it.copy(isLoading = false)
                }
                return@launch
            }
            viewModelState.update {
                when (val result = authenticationRepository.updateEmail(email = email)) {
                    is RepositoryResult.Success -> {
                        snackbarManager.showMessage(
                            R.string.successfully_updated_email,
                            SnackbarMessageType.Success
                        )
                        viewModelState.value.user?.let { user -> userRepository.saveUser(user) }
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

    private fun updateAddress() {
        viewModelScope.launch {
            snackbarManager.showMessage(
                R.string.successfully_updated_address,
                SnackbarMessageType.Success
            )
            viewModelState.value.user?.let { user -> userRepository.saveUser(user) }
        }
    }

    private fun updatePhone() {
        viewModelScope.launch {
            snackbarManager.showMessage(
                R.string.successfully_updated_phone,
                SnackbarMessageType.Success
            )
            viewModelState.value.user?.let { user -> userRepository.saveUser(user) }
        }
    }

    private fun observeUser() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            userRepository.observeUser(currentUserId).collect { user ->
                if (user == null) {
                    viewModelState.update { it.copy(error = ProfileError.EmptyProfile) }
                    return@collect
                }
                viewModelState.update { it.copy(user = user) }
            }
        }
    }
}
