package com.example.profile.settings.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 15/03/2023.
 */
sealed class ProfileSettingsError {
    object NoError : ProfileSettingsError()
    object EmptyProfile : ProfileSettingsError()
    object InvalidPassword : ProfileSettingsError()
    object NotMatchesPassword : ProfileSettingsError()
    data class AuthenticationError(val responseStatus: ResponseStatus) : ProfileSettingsError()
}