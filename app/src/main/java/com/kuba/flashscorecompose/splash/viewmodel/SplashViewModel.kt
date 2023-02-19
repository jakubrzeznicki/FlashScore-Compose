package com.kuba.flashscorecompose.splash.viewmodel

import androidx.lifecycle.ViewModel
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class SplashViewModel(private val authenticationRepository: AuthenticationDataSource) :
    ViewModel() {

    fun onAppStart(openHomeScreen: () -> Unit, openWelcomeScreen: () -> Unit) {
        if (authenticationRepository.hasUser) openHomeScreen() else openWelcomeScreen()
    }
}