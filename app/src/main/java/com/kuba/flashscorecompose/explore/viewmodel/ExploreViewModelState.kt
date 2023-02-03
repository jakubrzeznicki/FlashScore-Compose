package com.kuba.flashscorecompose.explore.viewmodel

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.explore.model.CoachCountry
import com.kuba.flashscorecompose.explore.model.ExploreError
import com.kuba.flashscorecompose.explore.model.ExploreUiState
import com.kuba.flashscorecompose.explore.model.TeamCountry
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerCountry
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
            FilterChip.Explore.Countries
        ),
    val liveFixtures: List<FixtureItem> = emptyList(),
    val favoriteFixtures: List<FixtureItem> = emptyList(),
    val filteredLiveFixtures: List<FixtureItem> = emptyList(),
    val favoriteTeams: List<TeamCountry> = emptyList(),
    val teams: List<TeamCountry> = emptyList(),
    val filteredTeams: List<TeamCountry> = emptyList(),
    val favoritePlayers: List<PlayerCountry> = emptyList(),
    val players: List<PlayerCountry> = emptyList(),
    val filteredPlayers: List<PlayerCountry> = emptyList(),
    val venues: List<Venue> = emptyList(),
    val filteredVenues: List<Venue> = emptyList(),
    val coaches: List<CoachCountry> = emptyList(),
    val filteredCoaches: List<CoachCountry> = emptyList(),
    val countries: List<Country> = emptyList(),
    val filteredCountries: List<Country> = emptyList()
) {
    fun toUiState(): ExploreUiState = when (exploreFilterChip) {
        is FilterChip.Explore.Fixtures -> {
            when {
                favoriteFixtures.isNotEmpty() && filteredLiveFixtures.isNotEmpty() ->
                    ExploreUiState.Fixtures.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredLiveFixtures,
                        favoriteFixtures
                    )
                favoriteFixtures.isNotEmpty() ->
                    ExploreUiState.Fixtures.HasOnlyFavoriteFixtures(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        favoriteFixtures
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
                favoriteTeams.isNotEmpty() && filteredTeams.isNotEmpty() ->
                    ExploreUiState.Teams.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredTeams,
                        favoriteTeams
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
                favoritePlayers.isNotEmpty() && filteredPlayers.isNotEmpty() ->
                    ExploreUiState.Players.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredPlayers,
                        favoritePlayers
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
        is FilterChip.Explore.Countries -> {
            when {
                filteredCountries.isNotEmpty() ->
                    ExploreUiState.Countries.HasFullData(
                        isLoading,
                        error,
                        exploreFilterChip,
                        exploreQuery,
                        exploreFilterChips,
                        filteredCountries
                    )
                else -> ExploreUiState.Countries.NoData(
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