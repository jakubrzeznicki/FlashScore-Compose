package com.example.onboarding.model

import com.example.common.utils.ResponseStatus


/**
 * Created by jrzeznicki on 07/02/2023.
 */
sealed class OnBoardingError {
    object NoError : OnBoardingError()
    data class RemoteError(val responseStatus: ResponseStatus) : OnBoardingError()
}
