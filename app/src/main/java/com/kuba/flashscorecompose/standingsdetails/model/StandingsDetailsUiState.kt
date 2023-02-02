package com.kuba.flashscorecompose.standingsdetails.model

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.ui.component.chips.FilterChip

/**
 * Created by jrzeznicki on 19/01/2023.
 */
interface StandingsDetailsUiState {
    val isLoading: Boolean
    val error: StandingsDetailsError
    val standingFilterChip: FilterChip.Standings
    val standingFilterChips: List<FilterChip.Standings>

    data class HasData(
        override val isLoading: Boolean,
        override val error: StandingsDetailsError,
        override val standingFilterChip: FilterChip.Standings,
        override val standingFilterChips: List<FilterChip.Standings>,
        val league: League,
        val standingsItems: List<StandingItem>
    ) : StandingsDetailsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: StandingsDetailsError,
        override val standingFilterChip: FilterChip.Standings,
        override val standingFilterChips: List<FilterChip.Standings>
    ) : StandingsDetailsUiState
}
