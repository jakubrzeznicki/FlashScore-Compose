package com.kuba.flashscorecompose.signin.model

import androidx.annotation.StringRes
import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 07/02/2023.
 */
sealed class SignInError {
    object NoError : SignInError()
    data class InvalidEmail(@StringRes val messageId: Int) : SignInError()
    data class BlankPassword(@StringRes val messageId: Int) : SignInError()
    data class AuthenticationError(
        val responseStatus: ResponseStatus,
        @StringRes val messageId: Int
    ) : SignInError()
}