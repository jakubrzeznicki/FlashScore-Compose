package com.example.teamdetails.fixturesteam.viewmodel

import com.example.model.fixture.FixtureItemWrapper
import com.example.teamdetails.fixturesteam.model.FixturesTeamError
import com.example.teamdetails.fixturesteam.model.FixturesTeamUiState
import com.example.ui.composables.chips.FilterChip

/**
 * Created by jrzeznicki on 30/01/2023.
 */
data class FixturesTeamViewModelState(
    val isLoading: Boolean = false,
    val error: FixturesTeamError = FixturesTeamError.NoError,
    val fixtureItemWrappers: List<FixtureItemWrapper> = emptyList(),
    val filteredFixtureItemWrappers: List<FixtureItemWrapper> = emptyList(),
    val fixtureFilterChip: FilterChip.Fixtures = FilterChip.Fixtures.Played,
    val fixtureFilterChips: List<FilterChip.Fixtures> =
        listOf(FilterChip.Fixtures.Played, FilterChip.Fixtures.Live, FilterChip.Fixtures.Upcoming)
) {
    fun toUiState(): FixturesTeamUiState = if (fixtureItemWrappers.isNotEmpty()) {
        FixturesTeamUiState.HasData(
            isLoading,
            error,
            fixtureFilterChip,
            fixtureFilterChips,
            filteredFixtureItemWrappers
        )
    } else {
        FixturesTeamUiState.NoData(isLoading, error, fixtureFilterChip, fixtureFilterChips)
    }
}
