package com.example.signin.signin.model

import androidx.annotation.StringRes
import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 07/02/2023.
 */
sealed class SignInError {
    object NoError : SignInError()
    data class InvalidEmail(@StringRes val messageId: Int) : SignInError()
    data class BlankPassword(@StringRes val messageId: Int) : SignInError()
    data class AuthenticationError(val responseStatus: ResponseStatus) : SignInError()
}
