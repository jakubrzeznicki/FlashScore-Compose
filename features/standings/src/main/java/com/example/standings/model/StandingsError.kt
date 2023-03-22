package com.example.standings.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 18/01/2023.
 */
sealed class StandingsError {
    object NoError : StandingsError()
    object EmptyLeague : StandingsError()
    data class RemoteError(val responseStatus: ResponseStatus) : StandingsError()
}
