package com.example.fixturedetails.headtohead.viewmodel

import com.example.fixturedetails.headtohead.model.HeadToHeadError
import com.example.fixturedetails.headtohead.model.HeadToHeadUiState
import com.example.fixturedetails.headtohead.model.StyledFixtureItem

/**
 * Created by jrzeznicki on 14/01/2023.
 */
data class HeadToHeadViewModelState(
    val isLoading: Boolean = false,
    val error: HeadToHeadError = HeadToHeadError.NoError,
    val homeTeamFixtures: List<StyledFixtureItem> = emptyList(),
    val awayTeamFixtures: List<StyledFixtureItem> = emptyList(),
    val h2hFixtures: List<StyledFixtureItem> = emptyList()
) {
    fun toUiState(): HeadToHeadUiState =
        if (h2hFixtures.isNotEmpty() || homeTeamFixtures.isNotEmpty() || awayTeamFixtures.isNotEmpty()) {
            HeadToHeadUiState.HasData(
                isLoading,
                error,
                homeTeamFixtures,
                awayTeamFixtures,
                h2hFixtures
            )
        } else {
            HeadToHeadUiState.NoData(isLoading, error)
        }
}
