package com.example.profile.navigation

import com.example.data.navigation.SignInBackStackType
import com.example.data.navigation.SignUpBackStackType
import com.example.data.navigation.WelcomeBackStackType

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface ProfileNavigator {
    fun openSignIn(signInBackStackType: SignInBackStackType)
    fun openSignUp(singUpBackStackType: SignUpBackStackType)
    fun openWelcome(welcomeBackStackType: WelcomeBackStackType)
}