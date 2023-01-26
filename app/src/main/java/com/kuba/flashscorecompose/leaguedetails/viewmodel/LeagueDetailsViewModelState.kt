package com.kuba.flashscorecompose.leaguedetails.viewmodel

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.leaguedetails.model.LeagueDetailsError
import com.kuba.flashscorecompose.leaguedetails.model.LeagueDetailsUiState
import java.time.LocalDate

/**
 * Created by jrzeznicki on 25/01/2023.
 */
data class LeagueDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: LeagueDetailsError = LeagueDetailsError.NoError,
    val date: LocalDate = LocalDate.now(),
    val league: League = League.EMPTY_LEAGUE,
    val fixtureItems: List<FixtureItem> = emptyList(),
    val filteredFixtureItems: List<FixtureItem> = emptyList(),
) {
    fun toUiState(): LeagueDetailsUiState = if (filteredFixtureItems.isNotEmpty()) {
        LeagueDetailsUiState.HasData(
            isLoading = isLoading,
            error = error,
            date = date,
            league = league,
            fixtureItems = filteredFixtureItems
        )
    } else {
        LeagueDetailsUiState.NoData(
            isLoading = isLoading,
            error = error,
            date = date,
            league = league
        )
    }
}