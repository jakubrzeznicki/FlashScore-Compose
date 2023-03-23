package com.example.fixturedetails.lineup.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 13/01/2023.
 */
sealed class LineupError {
    object NoError : LineupError()
    data class RemoteError(val responseStatus: ResponseStatus) : LineupError()
}
