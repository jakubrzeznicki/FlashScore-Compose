package com.kuba.flashscorecompose.standingsdetails.viewmodel

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.standingsdetails.model.StandingsDetailsError
import com.kuba.flashscorecompose.standingsdetails.model.StandingsDetailsUiState
import com.kuba.flashscorecompose.ui.component.chips.FilterChip

/**
 * Created by jrzeznicki on 19/01/2023.
 */
data class StandingsDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: StandingsDetailsError = StandingsDetailsError.NoError,
    val league: League = League.EMPTY_LEAGUE,
    val standingFilterChip: FilterChip.Standings = FilterChip.Standings.All,
    val standingsItems: List<StandingItem> = emptyList(),
    val filteredStandings: List<StandingItem> = emptyList()
) {
    fun toUiState(): StandingsDetailsUiState = if (filteredStandings.isNotEmpty()) {
        StandingsDetailsUiState.HasData(
            isLoading,
            error,
            standingFilterChip,
            league,
            filteredStandings
        )
    } else {
        StandingsDetailsUiState.NoData(isLoading, error, standingFilterChip)
    }
}