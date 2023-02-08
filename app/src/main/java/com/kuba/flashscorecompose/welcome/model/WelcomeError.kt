package com.kuba.flashscorecompose.welcome.model

import androidx.annotation.StringRes
import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 06/02/2023.
 */
sealed class WelcomeError {
    object NoError : WelcomeError()
    data class AuthenticationError(
        val responseStatus: ResponseStatus,
        @StringRes val messageId: Int
    ) : WelcomeError()
}