package com.example.standingsdetails.model

import com.example.model.standings.StandingItem
import com.example.ui.composables.chips.FilterChip

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
        val standingsItems: List<StandingItem>
    ) : StandingsDetailsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: StandingsDetailsError,
        override val standingFilterChip: FilterChip.Standings,
        override val standingFilterChips: List<FilterChip.Standings>
    ) : StandingsDetailsUiState
}
