package com.example.leaguedetails.model

import com.example.common.utils.ResponseStatus


/**
 * Created by jrzeznicki on 25/01/2023.
 */
sealed class LeagueDetailsError {
    object NoError : LeagueDetailsError()
    data class RemoteError(val responseStatus: ResponseStatus) : LeagueDetailsError()
}
