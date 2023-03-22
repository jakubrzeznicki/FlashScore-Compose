package com.example.data.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by jrzeznicki on 16/03/2023.
 */
@Parcelize
sealed interface WelcomeBackStackType : Parcelable {

    @Parcelize
    object Splash : WelcomeBackStackType

    @Parcelize
    object SignUp : WelcomeBackStackType

    @Parcelize
    object Profile : WelcomeBackStackType
}