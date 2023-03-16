package com.example.profile.settings.model

import androidx.annotation.StringRes
import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 15/03/2023.
 */
sealed class ProfileSettingsError {
    object NoError : ProfileSettingsError()
    data class InvalidEmail(@StringRes val messageId: Int) : ProfileSettingsError()
    data class InvalidPassword(@StringRes val messageId: Int) : ProfileSettingsError()
    data class NotMatchesPassword(@StringRes val messageId: Int) : ProfileSettingsError()
    data class AuthenticationError(val responseStatus: ResponseStatus) : ProfileSettingsError()
}