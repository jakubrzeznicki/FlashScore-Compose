package com.kuba.flashscorecompose.profile.details.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.profile.details.model.ProfileItem
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessage
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 08/02/2023.
 */
class ProfileDetailsViewModel(
    private val user: User,
    private val userRepository: UserDataSource,
    private val authenticationRepository: AuthenticationDataSource
) :
    ViewModel() {
    private val viewModelState = MutableStateFlow(ProfileDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

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
            is ProfileItem.Phone -> updatePhone(profileItem.value)
            is ProfileItem.Address -> updateAddress(profileItem.value)
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
        viewModelScope.launch {
            when (val result = authenticationRepository.updateName(name = name)) {
                is RepositoryResult.Success -> {
                    SnackbarManager.showMessage(R.string.successfully_updated_name)
                    userRepository.saveUser(viewModelState.value.user) // zmienic na update kazdego pola osobno
                }
                is RepositoryResult.Error -> {
                    val message = result.error.statusMessage?.let { statusMessage ->
                        SnackbarMessage.StringSnackbar(statusMessage)
                    } ?: SnackbarMessage.ResourceSnackbar(R.string.error_updated_name)
                    SnackbarManager.showMessage(message)
                }
            }
        }
    }

    private fun updateEmail(email: String) {
        viewModelScope.launch {
            when (val result = authenticationRepository.updateEmail(email = email)) {
                is RepositoryResult.Success -> {
                    SnackbarManager.showMessage(R.string.successfully_updated_email)
                    userRepository.saveUser(viewModelState.value.user) // zmienic na update kazdego pola osobno
                }
                is RepositoryResult.Error -> {
                    val message = result.error.statusMessage?.let { statusMessage ->
                        SnackbarMessage.StringSnackbar(statusMessage)
                    } ?: SnackbarMessage.ResourceSnackbar(R.string.error_updated_email)
                    SnackbarManager.showMessage(message)
                }
            }
        }
    }

    private fun updateAddress(address: String) {
        viewModelScope.launch {
            SnackbarManager.showMessage(R.string.successfully_updated_address)
            userRepository.saveUser(viewModelState.value.user) // zmienic na update kazdego pola osobno
        }
    }

    private fun updatePhone(phone: String) {
        viewModelScope.launch {
            SnackbarManager.showMessage(R.string.successfully_updated_phone)
            userRepository.saveUser(viewModelState.value.user) // zmienic na update kazdego pola osobno
        }
    }

    private fun observeUser() {
        viewModelScope.launch {
            val currentUserId = userRepository.getCurrentUserId()
            userRepository.observeUser(currentUserId).collect { user ->
                Log.d("TEST_LOG", "observe user - $user")
                viewModelState.update { it.copy(user = user) }
            }
        }
    }
}