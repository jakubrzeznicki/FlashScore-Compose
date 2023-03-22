package com.example.explore.model

import com.example.model.fixture.FixtureItemWrapper
import com.example.model.league.League
import com.example.model.player.PlayerWrapper
import com.example.model.team.CoachCountry
import com.example.model.team.TeamWrapper
import com.example.model.team.Venue
import com.example.ui.composables.chips.FilterChip

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
            val liveFixtures: List<FixtureItemWrapper>,
            val favoriteFixtures: List<FixtureItemWrapper>
        ) : ExploreUiState

        data class HasOnlyLiveFixtures(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val liveFixtures: List<FixtureItemWrapper>
        ) : ExploreUiState

        data class HasOnlyFavoriteFixtures(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val favoriteFixtures: List<FixtureItemWrapper>
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
            val teams: List<TeamWrapper>,
            val favoriteTeams: List<TeamWrapper>
        ) : ExploreUiState

        data class HasWithoutFavorite(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val teams: List<TeamWrapper>
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
            val players: List<PlayerWrapper>,
            val favoritePlayers: List<PlayerWrapper>
        ) : ExploreUiState

        data class HasWithoutFavorite(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val players: List<PlayerWrapper>
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

    sealed class Leagues {
        data class HasFullData(
            override val isLoading: Boolean,
            override val error: ExploreError,
            override val exploreFilterChip: FilterChip.Explore,
            override val exploreQuery: String,
            override val exploreFilterChips: List<FilterChip.Explore>,
            val leagues: List<League>
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
