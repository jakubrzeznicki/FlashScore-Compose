package com.example.signin.navigation

import com.example.data.navigation.HomeBackStackType
import com.example.data.navigation.OnBoardingBackStackType
import com.example.data.navigation.WelcomeBackStackType

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface SignInNavigator {
    fun openOnBoarding(onBoardingBackStackType: OnBoardingBackStackType)
    fun openHome(homeBackStackType: HomeBackStackType)
    fun openWelcome(welcomeBackStackType: WelcomeBackStackType)
    fun navigateUp()
}