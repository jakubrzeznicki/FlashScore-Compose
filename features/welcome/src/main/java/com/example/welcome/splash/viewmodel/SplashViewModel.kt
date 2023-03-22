package com.example.welcome.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.repository.AuthenticationDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class SplashViewModel(
    private val authenticationRepository: AuthenticationDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource
) : ViewModel() {

    fun onAppStart(openHomeScreen: () -> Unit, openWelcomeScreen: () -> Unit) {
        viewModelScope.launch {
            if (authenticationRepository.hasUser &&
                userPreferencesRepository.getCurrentUserId().isNotEmpty()
            ) {
                openHomeScreen()
            } else {
                openWelcomeScreen()
            }
        }
    }
}
