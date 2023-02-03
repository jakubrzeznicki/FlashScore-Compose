package com.kuba.flashscorecompose.explore.model

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerCountry
import com.kuba.flashscorecompose.ui.component.chips.FilterChip

/**
 * Created by jrzeznicki on 01/02/2023.
 */
interface ExploreUiState {
    val isLoading: Boolean
    val error: ExploreError
    val exploreFilterChip: FilterChip.Explore
    val exploreQuery: String
    val exploreFilterChips: List<FilterChip.Explore>

    sealed class Fixtures {
        data class HasFullData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val liveFixtures: List<FixtureItem>,
            val favoriteFixtures: List<FixtureItem>,

            ) : ExploreUiState

        data class HasOnlyLiveFixtures(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val liveFixtures: List<FixtureItem>
        ) : ExploreUiState

        data class HasOnlyFavoriteFixtures(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val favoriteFixtures: List<FixtureItem>
        ) : ExploreUiState

        data class NoData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>
        ) : ExploreUiState
    }

    sealed class Teams {
        data class HasFullData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val teams: List<TeamCountry>,
            val favoriteTeams: List<TeamCountry>
        ) : ExploreUiState

        data class HasWithoutFavorite(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val teams: List<TeamCountry>
        ) : ExploreUiState

        data class NoData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>
        ) : ExploreUiState
    }

    sealed class Players {
        data class HasFullData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val players: List<PlayerCountry>,
            val favoritePlayers: List<PlayerCountry>
        ) : ExploreUiState

        data class HasWithoutFavorite(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val players: List<PlayerCountry>,
        ) : ExploreUiState

        data class NoData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>
        ) : ExploreUiState
    }

    sealed class Venues {
        data class HasFullData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val venue: List<Venue>
        ) : ExploreUiState

        data class NoData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>
        ) : ExploreUiState
    }

    sealed class Coaches {
        data class HasFullData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val coaches: List<CoachCountry>
        ) : ExploreUiState

        data class NoData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>
        ) : ExploreUiState
    }

    sealed class Countries {
        data class HasFullData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val countries: List<Country>
        ) : ExploreUiState

        data class NoData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>
        ) : ExploreUiState
    }
}