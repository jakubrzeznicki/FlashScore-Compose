package com.kuba.flashscorecompose.profile.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.profile.details.model.ProfileItem
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager.showSnackbarMessage
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class ProfileDetailsViewModel(
    private val userRepository: UserDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val authenticationRepository: AuthenticationDataSource
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
                is ProfileItem.Name -> it.copy(user = it.user.copy(name = profileItem.value))
                is ProfileItem.Email -> it.copy(user = it.user.copy(email = profileItem.value))
                is ProfileItem.Phone -> it.copy(user = it.user.copy(phone = profileItem.value))
                is ProfileItem.Address -> it.copy(user = it.user.copy(address = profileItem.value))
            }
        }
    }

    private fun updateName(name: String) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            viewModelState.update {
                when (val result = authenticationRepository.updateName(name = name)) {
                    is RepositoryResult.Success -> {
                        SnackbarManager.showMessage(
                            R.string.successfully_updated_name,
                            SnackbarMessageType.Success
                        )
                        userRepository.saveUser(viewModelState.value.user)
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

    private fun updateEmail(email: String) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            viewModelState.update {
                when (val result = authenticationRepository.updateEmail(email = email)) {
                    is RepositoryResult.Success -> {
                        SnackbarManager.showMessage(
                            R.string.successfully_updated_email,
                            SnackbarMessageType.Success
                        )
                        userRepository.saveUser(viewModelState.value.user)
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

    private fun updateAddress() {
        viewModelScope.launch {
            SnackbarManager.showMessage(
                R.string.successfully_updated_address,
                SnackbarMessageType.Success
            )
            userRepository.saveUser(viewModelState.value.user)
        }
    }

    private fun updatePhone() {
        viewModelScope.launch {
            SnackbarManager.showMessage(
                R.string.successfully_updated_phone,
                SnackbarMessageType.Success
            )
            userRepository.saveUser(viewModelState.value.user)
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