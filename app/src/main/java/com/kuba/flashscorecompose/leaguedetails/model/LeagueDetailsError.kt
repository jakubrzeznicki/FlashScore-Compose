package com.kuba.flashscorecompose.leaguedetails.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 25/01/2023.
 */
sealed class LeagueDetailsError {
    object NoError : LeagueDetailsError()
    object EmptyLeague : LeagueDetailsError()
    data class RemoteError(val responseStatus: ResponseStatus) : LeagueDetailsError()
}