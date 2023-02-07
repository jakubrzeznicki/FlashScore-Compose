package com.kuba.flashscorecompose.signin.screen

import com.kuba.flashscorecompose.signin.model.TextFieldState
import com.kuba.flashscorecompose.signin.model.textFieldStateSaver
import java.util.regex.Pattern

/**
 * Created by jrzeznicki on 05/02/2023.
 */
private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

class EmailState :
    TextFieldState(validator = ::isEmailValid, errorFor = ::emailValidationError)

/**
 * Returns an error to be displayed or null if no error was found
 */
private fun emailValidationError(email: String): String {
    return "Invalid email: $email"
}

private fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
}

val EmailStateSaver = textFieldStateSaver(EmailState())
