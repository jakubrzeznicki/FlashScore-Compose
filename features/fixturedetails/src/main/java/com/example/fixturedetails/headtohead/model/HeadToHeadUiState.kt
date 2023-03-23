package com.example.fixturedetails.headtohead.model

/**
 * Created by jrzeznicki on 14/01/2023.
 */
interface HeadToHeadUiState {
    val isLoading: Boolean
    val error: HeadToHeadError

    data class HasData(
        override val isLoading: Boolean,
        override val error: HeadToHeadError,
        val homeTeamFixtures: List<StyledFixtureItem>,
        val awayTeamFixtures: List<StyledFixtureItem>,
        val h2hFixtures: List<StyledFixtureItem>
    ) : HeadToHeadUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: HeadToHeadError
    ) : HeadToHeadUiState
}
