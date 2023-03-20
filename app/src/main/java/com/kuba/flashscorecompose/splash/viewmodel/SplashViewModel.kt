package com.kuba.flashscorecompose.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
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
