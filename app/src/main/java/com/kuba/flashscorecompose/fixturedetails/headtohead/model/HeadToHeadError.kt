package com.kuba.flashscorecompose.fixturedetails.headtohead.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 14/01/2023.
 */
sealed class HeadToHeadError {
    object NoError : HeadToHeadError()
    data class RemoteError(val responseStatus: ResponseStatus) : HeadToHeadError()
}
