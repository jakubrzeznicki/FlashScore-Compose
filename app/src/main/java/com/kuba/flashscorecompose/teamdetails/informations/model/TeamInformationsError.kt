package com.kuba.flashscorecompose.teamdetails.informations.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 27/01/2023.
 */
sealed class TeamInformationsError {
    object NoError : TeamInformationsError()
    object EmptyTeam : TeamInformationsError()
    data class RemoteError(val responseStatus: ResponseStatus) : TeamInformationsError()
}
