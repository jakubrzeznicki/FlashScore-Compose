package com.kuba.flashscorecompose.welcome.model

/**
 * Created by jrzeznicki on 06/02/2023.
 */
sealed class WelcomeError {
    object NoError : WelcomeError()
    data class AuthenticationError(val message: String) : WelcomeError()
}