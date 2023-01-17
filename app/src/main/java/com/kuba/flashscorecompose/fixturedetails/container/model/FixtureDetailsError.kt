package com.kuba.flashscorecompose.fixturedetails.container.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 10/01/2023.
 */
sealed class FixtureDetailsError {
    object NoError : FixtureDetailsError()
    object EmptyDatabase : FixtureDetailsError()
}