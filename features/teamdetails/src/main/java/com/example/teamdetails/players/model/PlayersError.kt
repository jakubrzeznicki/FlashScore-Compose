package com.example.teamdetails.players.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 29/01/2023.
 */
sealed class PlayersError {
    object NoError : PlayersError()
    data class RemoteError(val responseStatus: ResponseStatus) : PlayersError()
}
