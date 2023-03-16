package com.example.teamdetails.fixturesteam.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 30/01/2023.
 */
sealed class FixturesTeamError {
    object NoError : FixturesTeamError()
    data class RemoteError(val responseStatus: ResponseStatus) : FixturesTeamError()
}
