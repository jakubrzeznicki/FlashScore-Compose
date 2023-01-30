package com.kuba.flashscorecompose.teamdetails.players.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 29/01/2023.
 */
sealed class PlayersError {
    object NoError : PlayersError()
    data class RemoteError(val responseStatus: ResponseStatus) : PlayersError()
}