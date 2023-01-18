package com.kuba.flashscorecompose.fixturedetails.headtohead.viewmodel

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.HeadToHeadError
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.HeadToHeadUiState

/**
 * Created by jrzeznicki on 14/01/2023.
 */
data class HeadToHeadViewModelState(
    val isLoading: Boolean = false,
    val error: HeadToHeadError = HeadToHeadError.NoError,
    val homeTeamFixtures: List<FixtureItem> = emptyList(),
    val awayTeamFixtures: List<FixtureItem> = emptyList(),
    val h2hFixtures: List<FixtureItem> = emptyList()
) {
    fun toUiState(): HeadToHeadUiState = if (h2hFixtures.isNotEmpty()) {
        HeadToHeadUiState.HasData(isLoading, error, homeTeamFixtures, awayTeamFixtures, h2hFixtures)
    } else {
        HeadToHeadUiState.NoData(isLoading, error)
    }
}