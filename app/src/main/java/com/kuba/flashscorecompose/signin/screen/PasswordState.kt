package com.kuba.flashscorecompose.signin.screen

import com.kuba.flashscorecompose.signin.model.TextFieldState

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class PasswordState :
    TextFieldState(validator = ::isPasswordValid, errorFor = ::passwordValidationError)

class ConfirmPasswordState(private val passwordState: PasswordState) : TextFieldState() {
    override val isValid
        get() = passwordAndConfirmationValid(passwordState.text, text)

    override fun getError(): String? {
        return if (showErrors()) {
            passwordConfirmationError()
        } else {
            null
        }
    }
}

private fun passwordAndConfirmationValid(password: String, confirmedPassword: String): Boolean {
    return isPasswordValid(password) && password == confirmedPassword
}

private fun isPasswordValid(password: String): Boolean {
    return password.length > 3
}

@Suppress("UNUSED_PARAMETER")
private fun passwordValidationError(password: String): String {
    return "Invalid password"
}

private fun passwordConfirmationError(): String {
    return "Passwords don't match"
}
