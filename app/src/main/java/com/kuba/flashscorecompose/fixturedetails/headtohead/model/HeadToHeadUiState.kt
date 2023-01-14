package com.kuba.flashscorecompose.fixturedetails.headtohead.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem

/**
 * Created by jrzeznicki on 14/01/2023.
 */
interface HeadToHeadUiState {
    val isLoading: Boolean
    val error: HeadToHeadError

    data class HasData(
        override val isLoading: Boolean,
        override val error: HeadToHeadError,
        val homeTeamFixtures: List<FixtureItem>,
        val awayTeamFixtures: List<FixtureItem>,
        val h2hFixtures: List<FixtureItem>
    ) : HeadToHeadUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: HeadToHeadError
    ) : HeadToHeadUiState
}