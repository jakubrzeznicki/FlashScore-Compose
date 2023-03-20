package com.kuba.flashscorecompose.utils

import android.util.Patterns
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

/**
 * Created by jrzeznicki on 21/01/2023.
 */
private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"
private const val MD5_ALGORITHM = "MD5"
private const val PAD_CHAR = '0'

fun String?.containsQuery(query: String) =
    this.orEmpty()
        .lowercase(Locale.getDefault())
        .contains(query.lowercase(Locale.getDefault()))

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() &&
            this.length >= MIN_PASS_LENGTH &&
            Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

fun String.passwordMatches(repeated: String): Boolean {
    return this == repeated
}

fun String.idFromParameter(): String {
    return this.substring(1, this.length - 1)
}

fun String.md5(): String {
    val byteArray = this.toByteArray()
    val messageDigest = MessageDigest.getInstance(MD5_ALGORITHM)
    val bigInteger = BigInteger(1, messageDigest.digest(byteArray))
    return bigInteger.toString(16).padStart(32, PAD_CHAR)
}