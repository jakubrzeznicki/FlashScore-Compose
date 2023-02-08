package com.kuba.flashscorecompose.splash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class SplashViewModel(private val authenticationRepository: AuthenticationDataSource) :
    ViewModel() {
    var authState: Boolean = false

    init {
        authState = getAuthState().value
    }

    fun onAppStart(openHomeScreen: () -> Unit, openWelcomeScreen: () -> Unit) {
        if (!authState) {
            Log.d("TEST_LOG", "Has user - true")
            openHomeScreen()
        } else {
            Log.d("TEST_LOG", "Has user - false")
            openWelcomeScreen()
        }
    }

    private fun getAuthState() = authenticationRepository.getAuthState(viewModelScope)
}