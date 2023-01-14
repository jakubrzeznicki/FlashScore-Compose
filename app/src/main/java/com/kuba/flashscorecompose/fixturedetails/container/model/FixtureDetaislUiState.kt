package com.kuba.flashscorecompose.fixturedetails.container.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup

/**
 * Created by jrzeznicki on 10/01/2023.
 */
interface FixtureDetaislUiState {
    val isLoading: Boolean
    val error: FixtureDetailsError

    data class HasData(
        override val isLoading: Boolean,
        override val error: FixtureDetailsError,
        val fixtureItem: FixtureItem
    ) : FixtureDetaislUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: FixtureDetailsError
    ) : FixtureDetaislUiState
}