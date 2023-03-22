package com.example.fixturedetails.container.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 07/03/2023.
 */
sealed class FixtureDetailsError {
    object NoError : FixtureDetailsError()
    object EmptyFixtureItem : FixtureDetailsError()
    data class RemoteError(val responseStatus: ResponseStatus) : FixtureDetailsError()
}
