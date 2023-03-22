package com.example.profile.navigation

import com.example.data.navigation.SignInBackStackType
import com.example.data.navigation.SignUpType
import com.example.data.navigation.WelcomeBackStackType

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface ProfileNavigator {
    fun openSignIn(signInBackStackType: SignInBackStackType)
    fun openSignUp(singUpType: SignUpType)
    fun openWelcome(welcomeBackStackType: WelcomeBackStackType)
}