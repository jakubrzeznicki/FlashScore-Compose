package com.kuba.flashscorecompose.playerdetails.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 01/02/2023.
 */
sealed class PlayerDetailsError {
    object NoError : PlayerDetailsError()
    object EmptyPlayer : PlayerDetailsError()
    data class RemoteError(val responseStatus: ResponseStatus) : PlayerDetailsError()
}