package com.kuba.flashscorecompose.standingsdetails.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 30/01/2023.
 */
sealed class StandingsDetailsError {
    object NoError : StandingsDetailsError()
    object EmptyStanding : StandingsDetailsError()
    data class RemoteError(val responseStatus: ResponseStatus) : StandingsDetailsError()
}