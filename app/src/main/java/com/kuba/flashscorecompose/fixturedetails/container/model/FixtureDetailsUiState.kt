package com.kuba.flashscorecompose.fixturedetails.container.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem

/**
 * Created by jrzeznicki on 10/01/2023.
 */
interface FixtureDetailsUiState {
    val error: FixtureDetailsError

    data class HasData(
        override val error: FixtureDetailsError,
        val fixtureItem: FixtureItem
    ) : FixtureDetailsUiState

    data class NoData(override val error: FixtureDetailsError) : FixtureDetailsUiState
}