package com.kuba.flashscorecompose.standings.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 18/01/2023.
 */
sealed class StandingsError {
    object NoError : StandingsError()
    data class RemoteError(val responseStatus: ResponseStatus) : StandingsError()
}