package com.kuba.flashscorecompose.teamdetails.fixturesteam.model

import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
import com.kuba.flashscorecompose.ui.component.chips.FilterChip

/**
 * Created by jrzeznicki on 30/01/2023.
 */
interface FixturesTeamUiState {
    val isLoading: Boolean
    val error: FixturesTeamError
    val fixtureFilterChip: FilterChip.Fixtures
    val fixtureFilterChips: List<FilterChip.Fixtures>

    data class HasData(
        override val isLoading: Boolean,
        override val error: FixturesTeamError,
        override val fixtureFilterChip: FilterChip.Fixtures,
        override val fixtureFilterChips: List<FilterChip.Fixtures>,
        val fixtureItemWrappers: List<FixtureItemWrapper>
    ) : FixturesTeamUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: FixturesTeamError,
        override val fixtureFilterChip: FilterChip.Fixtures,
        override val fixtureFilterChips: List<FilterChip.Fixtures>
    ) : FixturesTeamUiState
}
