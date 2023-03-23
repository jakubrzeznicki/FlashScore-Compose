package com.example.data.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by jrzeznicki on 16/03/2023.
 */
@Parcelize
sealed interface SignInBackStackType : Parcelable {

    @Parcelize
    object Profile : SignInBackStackType

    @Parcelize
    object Welcome : SignInBackStackType
}