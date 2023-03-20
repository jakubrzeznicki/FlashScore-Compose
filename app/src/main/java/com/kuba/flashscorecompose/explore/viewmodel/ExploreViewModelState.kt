package com.kuba.flashscorecompose.explore.viewmodel

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.explore.model.CoachCountry
import com.kuba.flashscorecompose.explore.model.ExploreError
import com.kuba.flashscorecompose.explore.model.ExploreUiState
import com.kuba.flashscorecompose.explore.model.TeamWrapper
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerWrapper
import com.kuba.flashscorecompose.ui.component.chips.FilterChip

/**
 * Created by jrzeznicki on 01/02/2023.
 */
data class ExploreViewModelState(
    val isLoading: Boolean = false,
    val error: ExploreError = ExploreError.NoError,
    val exploreFilterChip: FilterChip.Explore = FilterChip.Explore.Fixtures,
    val exploreQuery: String = "",
    val exploreFilterChips: List<FilterChip.Explore> =
        listOf(
            FilterChip.Explore.Fixtures,
            FilterChip.Explore.Teams,
            FilterChip.Explore.Players,
            FilterChip.Explore.Coaches,
            FilterChip.Explore.Venues,
            FilterChip.Explore.Leagues
        ),
    val liveFixtures: List<FixtureItemWrapper> = emptyList(),
    val favoriteFixtures: List<FixtureItemWrapper> = emptyList(),
    val filteredLiveFixtures: List<FixtureItemWrapper> = emptyList(),
    val filteredFavoriteFixtures: List<FixtureItemWrapper> = emptyList(),
    val favoriteTeams: List<TeamWrapper> = emptyList(),
    val teams: List<TeamWrapper> = emptyList(),
    val filteredTeams: List<TeamWrapper> = emptyList(),
    val filteredFavoriteTeams: List<TeamWrapper> = emptyList(),
    val favoritePlayers: List<PlayerWrapper> = emptyList(),
    val players: List<PlayerWrapper> = emptyList(),
    val filteredPlayers: List<PlayerWrapper> = emptyList(),
    val filteredFavoritePlayers: List<PlayerWrapper> = emptyList(),
    val venues: List<Venue> = emptyList(),
    val filteredVenues: List<Venue> = emptyList(),
    val coaches: List<CoachCountry> = emptyList(),
    val filteredCoaches: List<CoachCountry> = emptyList(),
    val leagues: List<League> = emptyList(),
    val filteredLeagues: List<League> = emptyList()
) {
    fun toUiState(): ExploreUiState = when (exploreFilterChip) {
        is FilterChip.Explore.Fixtures -> {
            when {
                filteredFavoriteFixtures.isNotEmpty() && filteredLiveFixtures.isNotEmpty() ->
                    ExploreUiState.Fixtures.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredLiveFixtures,
                        filteredFavoriteFixtures
                    )
                filteredFavoriteFixtures.isNotEmpty() ->
                    ExploreUiState.Fixtures.HasOnlyFavoriteFixtures(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredFavoriteFixtures
                    )
                filteredLiveFixtures.isNotEmpty() ->
                    ExploreUiState.Fixtures.HasOnlyLiveFixtures(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredLiveFixtures
                    )
                else -> ExploreUiState.Fixtures.NoData(
                    isLoading,
                    error,
                    exploreFilterChip,
                    exploreQuery,
                    exploreFilterChips
                )
            }
        }
        is FilterChip.Explore.Teams -> {
            when {
                filteredFavoriteTeams.isNotEmpty() && filteredTeams.isNotEmpty() ->
                    ExploreUiState.Teams.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredTeams,
                        filteredFavoriteTeams
                    )
                filteredTeams.isNotEmpty() ->
                    ExploreUiState.Teams.HasWithoutFavorite(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredTeams,
                    )
                else -> ExploreUiState.Teams.NoData(
                    isLoading,
                    error,
                    exploreFilterChip,
                    exploreQuery,
                    exploreFilterChips
                )
            }
        }
        is FilterChip.Explore.Players -> {
            when {
                filteredFavoritePlayers.isNotEmpty() && filteredPlayers.isNotEmpty() ->
                    ExploreUiState.Players.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredPlayers,
                        filteredFavoritePlayers
                    )
                filteredTeams.isNotEmpty() ->
                    ExploreUiState.Players.HasWithoutFavorite(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredPlayers,
                    )
                else -> ExploreUiState.Players.NoData(
                    isLoading,
                    error,
                    exploreFilterChip,
                    exploreQuery,
                    exploreFilterChips
                )
            }
        }
        is FilterChip.Explore.Venues -> {
            when {
                filteredVenues.isNotEmpty() ->
                    ExploreUiState.Venues.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredVenues,
                    )
                else -> ExploreUiState.Venues.NoData(
                    isLoading,
                    error,
                    exploreFilterChip,
                    exploreQuery,
                    exploreFilterChips
                )
            }
        }
        is FilterChip.Explore.Coaches -> {
            when {
                filteredCoaches.isNotEmpty() ->
                    ExploreUiState.Coaches.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredCoaches
                    )
                else -> ExploreUiState.Coaches.NoData(
                    isLoading,
                    error,
                    exploreFilterChip,
                    exploreQuery,
                    exploreFilterChips
                )
            }
        }
        is FilterChip.Explore.Leagues -> {
            when {
                filteredLeagues.isNotEmpty() ->
                    ExploreUiState.Leagues.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredLeagues
                    )
                else -> ExploreUiState.Leagues.NoData(
                    isLoading,
                    error,
                    exploreFilterChip,
                    exploreQuery,
                    exploreFilterChips
                )
            }
        }
    }
}