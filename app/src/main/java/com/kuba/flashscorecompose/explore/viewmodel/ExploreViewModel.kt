package com.kuba.flashscorecompose.explore.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.league.LeagueDataSource
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.data.team.information.TeamDataSource
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.explore.model.CoachCountry
import com.kuba.flashscorecompose.explore.model.ExploreError
import com.kuba.flashscorecompose.explore.model.TeamWrapper
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerWrapper
import com.kuba.flashscorecompose.ui.component.chips.FilterChip
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.containsQuery
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 02/02/2023.
 */
class ExploreViewModel(
    private val fixturesRepository: FixturesDataSource,
    private val teamRepository: TeamDataSource,
    private val playersRepository: PlayersDataSource,
    private val countryRepository: CountryDataSource,
    private val leagueRepository: LeagueDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(ExploreViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observeLeagues()
        observeFixtures()
        observeFavoriteFixtures()
        observeTeams()
        observePlayers()
        observeCoaches()
        observeVenues()
    }

    fun refresh() {
        refreshFixtures()
    }

    private fun observeLeagues() {
        viewModelScope.launch {
            leagueRepository.observeLeagues().collect { leagues ->
                val filteredLeagues = filterLeagues(leagues)
                viewModelState.update {
                    it.copy(leagues = leagues, filteredLeagues = filteredLeagues)
                }
            }
        }
    }

    private fun observeFixtures() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferencesFlow =
                userPreferencesRepository.observeUserPreferences(currentUserId)
            val fixturesFlow = fixturesRepository.observeFixturesLive()
            combine(flow = fixturesFlow, flow2 = userPreferencesFlow) { fixtures, userPreferences ->
                val favoriteFixtureIds = userPreferences.favoriteFixtureIds
                val fixtureItemWrappers = fixtures.map {
                    FixtureItemWrapper(
                        fixtureItem = it,
                        isFavorite = favoriteFixtureIds.contains(it.id)
                    )
                }
                val filteredFixtureItemWrappers = filterFixtures(fixtureItemWrappers)
                viewModelState.update {
                    it.copy(
                        liveFixtures = fixtureItemWrappers,
                        filteredLiveFixtures = filteredFixtureItemWrappers
                    )
                }
            }
        }
    }

    private fun observeFavoriteFixtures() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferences = userPreferencesRepository.observeUserPreferences(currentUserId)
            userPreferences.map { userPreferences ->
                val favoriteFixtureIds = userPreferences.favoriteFixtureIds
                fixturesRepository.observeFavoriteFixtures(favoriteFixtureIds).collect { fixtures ->
                    val fixtureItemWrappers = fixtures.map {
                        FixtureItemWrapper(
                            fixtureItem = it,
                            isFavorite = favoriteFixtureIds.contains(it.id)
                        )
                    }
                    val filteredFixtureItemWrappers = filterFavoriteFixtures(fixtureItemWrappers)
                    viewModelState.update {
                        it.copy(
                            favoriteFixtures = fixtureItemWrappers,
                            filteredFavoriteFixtures = filteredFixtureItemWrappers
                        )
                    }
                }
            }.collect()
        }
    }

    private fun observeTeams() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferencesFlow =
                userPreferencesRepository.observeUserPreferences(currentUserId)
            val teamsFlow = teamRepository.observeTeams()
            combine(
                flow = teamsFlow,
                flow2 = userPreferencesFlow
            ) { teams, userPreferences ->
                Log.d("TEST_LOG", "observeTeams - userId - ${userPreferences.userId}")
                val favoriteTeamIds = userPreferences.favoriteTeamIds
                val countries = countryRepository.getCountries()
                val teamWrappers = teams.toTeamWrappers(countries, favoriteTeamIds)
                val filteredTeamWrappers = filterTeams(teamWrappers)
                val favoriteTeams = teams.filter { favoriteTeamIds.contains(it.id) }
                val favoriteTeamWrappers = favoriteTeams.toTeamWrappers(countries, favoriteTeamIds)
                val filteredFavoriteTeamWrappers = filterFavoriteTeams(favoriteTeamWrappers)
                viewModelState.update {
                    it.copy(
                        teams = teamWrappers,
                        filteredTeams = filteredTeamWrappers,
                        favoriteTeams = favoriteTeamWrappers,
                        filteredFavoriteTeams = filteredFavoriteTeamWrappers
                    )
                }
            }.collect()
        }
    }

    private fun List<Team>.toTeamWrappers(
        countries: List<Country>,
        favoriteTeamIds: List<Int>
    ): List<TeamWrapper> = map {
        //Log.d("TEST_LOG", "team Country - ${it.country}")
        TeamWrapper(
            team = it,
            country = countries.firstOrNull { country -> country.name == it.country }
                ?: Country.EMPTY_COUNTRY,
            isFavorite = favoriteTeamIds.contains(it.id)
        )
    }

    private fun observePlayers() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferencesFlow =
                userPreferencesRepository.observeUserPreferences(currentUserId)
            val playersFlow = playersRepository.observePlayers()
            combine(
                flow = playersFlow,
                flow2 = userPreferencesFlow
            ) { players, userPreferences ->
                Log.d("TEST_LOG", "observePlayers - userId - ${userPreferences.userId}")
                val favoritePlayerIds = userPreferences.favoritePlayerIds
                val countries = countryRepository.getCountries()
                val playerWrappers = players.toPlayerWrappers(countries, favoritePlayerIds)
                val filteredPlayerWrappers = filterPlayers(playerWrappers)
                val favoritePlayers = players.filter { favoritePlayerIds.contains(it.id) }
                val favoritePlayerWrappers =
                    favoritePlayers.toPlayerWrappers(countries, favoritePlayerIds)
                val filteredFavoritePlayerWrappers = filterFavoritePlayers(favoritePlayerWrappers)
                viewModelState.update {
                    it.copy(
                        players = playerWrappers,
                        filteredPlayers = filteredPlayerWrappers,
                        favoritePlayers = favoritePlayerWrappers,
                        filteredFavoritePlayers = filteredFavoritePlayerWrappers
                    )
                }
            }.collect()
        }
    }

    private fun List<Player>.toPlayerWrappers(
        countries: List<Country>,
        favoritePlayerIds: List<Int>
    ): List<PlayerWrapper> = map {
        PlayerWrapper(
            player = it,
            country = countries.firstOrNull { country -> country.name == it.nationality }
                ?: Country.EMPTY_COUNTRY,
            isFavorite = favoritePlayerIds.contains(it.id)
        )
    }

    private fun observeVenues() {
        viewModelScope.launch {
            teamRepository.observeVenues().collect { venues ->
                val filteredVenues = filterVenues(venues)
                viewModelState.update {
                    it.copy(venues = venues, filteredVenues = filteredVenues)
                }
            }
        }
    }

    private fun observeCoaches() {
        viewModelScope.launch {
            teamRepository.observeCoaches().collect { coaches ->
                val countries = countryRepository.getCountries()
                val coachCountries = coaches.map {
                    CoachCountry(
                        coach = it,
                        country = countries.firstOrNull { country -> country.name == it.nationality }
                            ?: Country.EMPTY_COUNTRY)
                }
                val filteredCoaches = filterCoaches(coachCountries)
                viewModelState.update {
                    it.copy(coaches = coachCountries, filteredCoaches = filteredCoaches)
                }
            }
        }
    }

    private fun refreshFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesLive()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            isLoading = false,
                            error = ExploreError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    fun updateExploreQuery(newQuery: String) {
        viewModelState.update {
            when (viewModelState.value.exploreFilterChip) {
                is FilterChip.Explore.Fixtures -> {
                    val filteredFixtures = filterFixtures(query = newQuery)
                    it.copy(filteredLiveFixtures = filteredFixtures, exploreQuery = newQuery)
                }
                is FilterChip.Explore.Teams -> {
                    val filteredTeams = filterTeams(query = newQuery)
                    val filteredFavoriteTeams = filterFavoriteTeams(query = newQuery)
                    it.copy(
                        filteredTeams = filteredTeams,
                        filteredFavoriteTeams = filteredFavoriteTeams,
                        exploreQuery = newQuery
                    )
                }
                is FilterChip.Explore.Venues -> {
                    val filteredVenues = filterVenues(query = newQuery)
                    it.copy(filteredVenues = filteredVenues, exploreQuery = newQuery)
                }
                is FilterChip.Explore.Players -> {
                    val filteredPlayers = filterPlayers(query = newQuery)
                    val filteredFavoritePlayers = filterFavoritePlayers(query = newQuery)
                    it.copy(
                        filteredPlayers = filteredPlayers,
                        filteredFavoritePlayers = filteredFavoritePlayers,
                        exploreQuery = newQuery
                    )
                }
                is FilterChip.Explore.Coaches -> {
                    val filteredCoaches = filterCoaches(query = newQuery)
                    it.copy(filteredCoaches = filteredCoaches, exploreQuery = newQuery)
                }
                is FilterChip.Explore.Leagues -> {
                    val filteredCountries = filterLeagues(query = newQuery)
                    it.copy(filteredLeagues = filteredCountries, exploreQuery = newQuery)
                }
            }
        }
    }

    fun changeExploreView(newExploreFilterChip: FilterChip.Explore) {
        viewModelState.update {
            it.copy(exploreFilterChip = newExploreFilterChip)
        }
    }

    fun addTeamToFavorite(teamWrapper: TeamWrapper) {
        viewModelScope.launch {
            val favoriteTeams =
                viewModelState.value.favoriteTeams.toMutableList()
            if (teamWrapper.isFavorite) {
                favoriteTeams.remove(teamWrapper)
            } else {
                favoriteTeams.add(teamWrapper.copy(isFavorite = true))
            }
            viewModelState.update {
                it.copy(
                    favoriteTeams = favoriteTeams.toList(),
                    filteredFavoriteTeams = filterFavoriteTeams(favoriteTeams.toList())
                )
            }
            userPreferencesRepository.saveFavoriteTeamIds(favoriteTeams.map { it.team.id })
        }
    }

    fun addPlayerToFavorite(playerWrapper: PlayerWrapper) {
        viewModelScope.launch {
            val favoritePlayers =
                viewModelState.value.favoritePlayers.toMutableList()
            if (playerWrapper.isFavorite) {
                favoritePlayers.remove(playerWrapper)
            } else {
                favoritePlayers.add(playerWrapper.copy(isFavorite = true))
            }
            viewModelState.update {
                it.copy(
                    favoritePlayers = favoritePlayers.toList(),
                    filteredFavoritePlayers = filterFavoritePlayers(favoritePlayers.toList())
                )
            }
            userPreferencesRepository.saveFavoritePlayerIds(favoritePlayers.map { it.player.id })
        }
    }

    fun addFixtureToFavorite(fixtureItemWrapper: FixtureItemWrapper) {
        viewModelScope.launch {
            val favoriteFixtures =
                viewModelState.value.favoriteFixtures.toMutableList()
            if (fixtureItemWrapper.isFavorite) {
                favoriteFixtures.remove(fixtureItemWrapper)
            } else {
                favoriteFixtures.add(fixtureItemWrapper.copy(isFavorite = true))
            }
            viewModelState.update {
                it.copy(
                    favoriteFixtures = favoriteFixtures.toList(),
                    filteredFavoriteFixtures = filterFavoriteFixtures(favoriteFixtures.toList())
                )
            }
            userPreferencesRepository.saveFavoriteFixturesIds(
                favoriteFixtures.map { it.fixtureItem.id }
            )
        }
    }

    private fun filterFixtures(
        fixtureItems: List<FixtureItemWrapper> = viewModelState.value.liveFixtures,
        query: String = viewModelState.value.exploreQuery
    ): List<FixtureItemWrapper> {
        return fixtureItems.filter {
            it.fixtureItem.homeTeam.name.containsQuery(query)
                    || it.fixtureItem.awayTeam.name.containsQuery(query)
                    || it.fixtureItem.league.name.containsQuery(query)
                    || it.fixtureItem.league.countryName.containsQuery(query)
        }
    }

    private fun filterTeams(
        teams: List<TeamWrapper> = viewModelState.value.teams,
        query: String = viewModelState.value.exploreQuery
    ): List<TeamWrapper> {
        return teams.filter {
            it.team.name.containsQuery(query) || it.team.country.containsQuery(query)
        }
    }

    private fun filterFavoriteTeams(
        favoriteTeams: List<TeamWrapper> = viewModelState.value.favoriteTeams,
        query: String = viewModelState.value.exploreQuery
    ): List<TeamWrapper> {
        return favoriteTeams.filter {
            it.team.name.containsQuery(query) || it.team.country.containsQuery(query)
        }
    }

    private fun filterVenues(
        venues: List<Venue> = viewModelState.value.venues,
        query: String = viewModelState.value.exploreQuery
    ): List<Venue> {
        return venues.filter {
            it.name.containsQuery(query) || it.city.containsQuery(query)
        }
    }

    private fun filterLeagues(
        leagues: List<League> = viewModelState.value.leagues,
        query: String = viewModelState.value.exploreQuery
    ): List<League> {
        return leagues.filter {
            it.name.containsQuery(query) || it.countryName.containsQuery(
                query
            )
        }
    }

    private fun filterCoaches(
        coaches: List<CoachCountry> = viewModelState.value.coaches,
        query: String = viewModelState.value.exploreQuery
    ): List<CoachCountry> {
        return coaches.filter {
            it.coach.name.containsQuery(query) || it.coach.nationality.containsQuery(query)
                    || it.coach.firstname.containsQuery(query)
                    || it.coach.lastname.containsQuery(query)
        }
    }

    private fun filterPlayers(
        players: List<PlayerWrapper> = viewModelState.value.players,
        query: String = viewModelState.value.exploreQuery
    ): List<PlayerWrapper> {
        return players.filter {
            it.player.name.containsQuery(query) || it.player.nationality.containsQuery(query)
                    || it.player.firstname.containsQuery(query)
                    || it.player.lastname.containsQuery(query)
        }
    }

    private fun filterFavoritePlayers(
        favoritePlayers: List<PlayerWrapper> = viewModelState.value.favoritePlayers,
        query: String = viewModelState.value.exploreQuery
    ): List<PlayerWrapper> {
        return favoritePlayers.filter {
            it.player.name.containsQuery(query) || it.player.nationality.containsQuery(query)
                    || it.player.firstname.containsQuery(query)
                    || it.player.lastname.containsQuery(query)
        }
    }

    private fun filterFavoriteFixtures(
        favoriteFixtures: List<FixtureItemWrapper> = viewModelState.value.favoriteFixtures,
        query: String = viewModelState.value.exploreQuery
    ): List<FixtureItemWrapper> {
        return favoriteFixtures.filter {
            it.fixtureItem.homeTeam.name.containsQuery(query)
                    || it.fixtureItem.awayTeam.name.containsQuery(query)
                    || it.fixtureItem.league.name.containsQuery(query)
                    || it.fixtureItem.league.countryName.containsQuery(query)
        }
    }
}