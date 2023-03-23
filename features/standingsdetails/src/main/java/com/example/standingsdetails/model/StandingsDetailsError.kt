package com.example.standingsdetails.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 30/01/2023.
 */
sealed class StandingsDetailsError {
    object NoError : StandingsDetailsError()
    object EmptyStanding : StandingsDetailsError()
    data class RemoteError(val responseStatus: ResponseStatus) : StandingsDetailsError()
}
