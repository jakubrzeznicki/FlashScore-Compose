package com.kuba.flashscorecompose.standingsdetails.viewmodel

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.standingsdetails.model.StandingFilterButton
import com.kuba.flashscorecompose.standingsdetails.model.StandingsDetailsUiState

/**
 * Created by jrzeznicki on 19/01/2023.
 */
data class StandingsDetailsViewModelState(
    val league: League = League.EMPTY_LEAGUE,
    val standingFilterButton: StandingFilterButton = StandingFilterButton.All,
    val standingsItems: List<StandingItem> = emptyList(),
    val filteredStandings: List<StandingItem> = emptyList()
) {
    fun toUiState(): StandingsDetailsUiState = StandingsDetailsUiState(
        league = league,
        standingFilterButton = standingFilterButton,
        standingsItems = filteredStandings
    )
}