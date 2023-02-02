package com.kuba.flashscorecompose.teamdetails.fixturesteam.viewmodel

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.teamdetails.fixturesteam.model.FixturesTeamError
import com.kuba.flashscorecompose.teamdetails.fixturesteam.model.FixturesTeamUiState
import com.kuba.flashscorecompose.ui.component.chips.FilterChip

/**
 * Created by jrzeznicki on 30/01/2023.
 */
data class FixturesTeamViewModelState(
    val isLoading: Boolean = false,
    val error: FixturesTeamError = FixturesTeamError.NoError,
    val fixtures: List<FixtureItem> = emptyList(),
    val filteredFixtures: List<FixtureItem> = emptyList(),
    val fixtureFilterChip: FilterChip.Fixtures = FilterChip.Fixtures.Played,
    val fixtureFilterChips: List<FilterChip.Fixtures> =
        listOf(FilterChip.Fixtures.Played, FilterChip.Fixtures.Live, FilterChip.Fixtures.Upcoming),
) {
    fun toUiState(): FixturesTeamUiState = if (fixtures.isNotEmpty()) {
        FixturesTeamUiState.HasData(
            isLoading,
            error,
            fixtureFilterChip,
            fixtureFilterChips,
            filteredFixtures
        )
    } else {
        FixturesTeamUiState.NoData(isLoading, error, fixtureFilterChip, fixtureFilterChips)
    }
}