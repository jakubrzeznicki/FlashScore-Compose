package com.kuba.flashscorecompose.splash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class SplashViewModel(private val authenticationRepository: AuthenticationDataSource) :
    ViewModel() {

    fun onAppStart(openHomeScreen: () -> Unit, openWelcomeScreen: () -> Unit) {
        if (authenticationRepository.hasUser) {
            Log.d("TEST_LOG", "Has user - true")
            openHomeScreen()
        } else {
            Log.d("TEST_LOG", "Has user - false")
            openWelcomeScreen()
        }
    }
}