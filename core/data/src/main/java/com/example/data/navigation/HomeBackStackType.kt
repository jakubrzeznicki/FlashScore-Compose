package com.example.data.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by jrzeznicki on 16/03/2023.
 */
@Parcelize
sealed interface HomeBackStackType : Parcelable {

    @Parcelize
    object Welcome : HomeBackStackType

    @Parcelize
    object OnBoarding : HomeBackStackType

    @Parcelize
    object Splash : HomeBackStackType

    @Parcelize
    object SignIn : HomeBackStackType
}