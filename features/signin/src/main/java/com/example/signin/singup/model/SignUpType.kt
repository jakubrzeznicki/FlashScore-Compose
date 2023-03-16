package com.example.signin.singup.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by jrzeznicki on 13/02/2023.
 */
@Parcelize
sealed interface SignUpType : Parcelable {
    @Parcelize
    object New : SignUpType

    @Parcelize
    object Anonymous : SignUpType
}
