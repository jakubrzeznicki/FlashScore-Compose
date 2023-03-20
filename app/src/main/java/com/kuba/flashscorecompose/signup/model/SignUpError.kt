package com.kuba.flashscorecompose.signup.model

import androidx.annotation.StringRes
import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 07/02/2023.
 */
sealed class SignUpError {
    object NoError : SignUpError()
    data class InvalidEmail(@StringRes val messageId: Int) : SignUpError()
    data class InvalidPassword(@StringRes val messageId: Int) : SignUpError()
    data class NotMatchesPassword(@StringRes val messageId: Int) : SignUpError()
    data class AuthenticationError(val responseStatus: ResponseStatus) : SignUpError()
}
