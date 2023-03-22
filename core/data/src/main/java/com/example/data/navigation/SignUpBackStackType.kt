package com.example.data.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by jrzeznicki on 13/02/2023.
 */
@Parcelize
sealed interface SignUpBackStackType : Parcelable {

    @Parcelize
    object New : SignUpBackStackType

    @Parcelize
    object Anonymous : SignUpBackStackType
}
