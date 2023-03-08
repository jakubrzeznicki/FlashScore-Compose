package com.kuba.flashscorecompose.data.user.model

import android.net.Uri

/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val address: String = "",
    val photoUri: Uri = Uri.EMPTY,
    val isAnonymous: Boolean = false,
    val isOnBoardingCompleted: Boolean = false
)
