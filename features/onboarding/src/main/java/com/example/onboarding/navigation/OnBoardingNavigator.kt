package com.example.onboarding.navigation

import com.example.data.navigation.HomeBackStackType

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface OnBoardingNavigator {
    fun openHome(homeBackStackType: HomeBackStackType)
}