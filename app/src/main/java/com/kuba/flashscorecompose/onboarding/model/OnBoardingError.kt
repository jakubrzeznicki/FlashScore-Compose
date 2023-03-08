package com.kuba.flashscorecompose.onboarding.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 07/02/2023.
 */
sealed class OnBoardingError {
    object NoError : OnBoardingError()
    data class RemoteError(val responseStatus: ResponseStatus) : OnBoardingError()
}
