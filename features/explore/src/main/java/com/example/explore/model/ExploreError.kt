package com.example.explore.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 27/01/2023.
 */
sealed class ExploreError {
    object NoError : ExploreError()
    object EmptyTeam : ExploreError()
    data class RemoteError(val responseStatus: ResponseStatus) : ExploreError()
}
