package com.example.welcome.navigation

import com.example.data.navigation.HomeBackStackType
import com.example.data.navigation.SignUpBackStackType
import com.example.data.navigation.WelcomeBackStackType

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface WelcomeNavigator {
    fun openHome(homeBackStackType: HomeBackStackType)
    fun openWelcome(welcomeBackStackType: WelcomeBackStackType)
    fun openSignIn()
    fun openSignUp(signUpBackStackType: SignUpBackStackType)
    fun openOnBoarding()
}