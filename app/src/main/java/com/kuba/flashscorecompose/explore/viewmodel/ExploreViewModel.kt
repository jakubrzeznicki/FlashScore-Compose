package com.kuba.flashscorecompose.explore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.data.team.information.TeamDataSource
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.explore.model.CoachCountry
import com.kuba.flashscorecompose.explore.model.ExploreError
import com.kuba.flashscorecompose.explore.model.TeamCountry
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerCountry
import com.kuba.flashscorecompose.ui.component.chips.FilterChip
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
    private val countryRepository: CountryDataSource
) : ViewModel() {
    private val viewModelState = MutableStateFlow(ExploreViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observeCountries()
        observeLiveFixtures()
        observeTeams()
        observePlayers()
        observeCoaches()
        observeVenues()
    }

    fun refresh() {
        refreshCountries()
        refreshFixtures()
    }

    private fun observeCountries() {
        viewModelScope.launch {
            countryRepository.observeCountries().collect { countries ->
                val filteredCountries = filterCountries(countries)
                viewModelState.update {
                    it.copy(countries = countries, filteredCountries = filteredCountries)
                }
            }
        }
    }

    private fun refreshCountries() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = countryRepository.loadCountries()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = ExploreError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    private fun observeLiveFixtures() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesLive().collect { fixtureItems ->
                val filteredFixtures = filterFixtures(fixtureItems)
                viewModelState.update {
                    it.copy(liveFixtures = fixtureItems, filteredLiveFixtures = filteredFixtures)
                }
            }
        }
    }

    private fun observeTeams() {
        viewModelScope.launch {
            val countries = countryRepository.getCountries()
            teamRepository.observeTeams().collect { teams ->
                val teamCountries = teams.map {
                    TeamCountry(
                        team = it,
                        country = countries.firstOrNull { country -> country.name == it.country }
                            ?: Country.EMPTY_COUNTRY)
                }
                val filteredTeams = filterTeams(teamCountries)
                viewModelState.update {
                    it.copy(teams = teamCountries, filteredTeams = filteredTeams)
                }
            }
        }
    }

    private fun observePlayers() {
        viewModelScope.launch {
            val countries = countryRepository.getCountries()
            playersRepository.observePlayers().collect { players ->
                val playerCountries = players.map {
                    PlayerCountry(
                        player = it,
                        country = countries.firstOrNull { country -> country.name == it.nationality }
                            ?: Country.EMPTY_COUNTRY)
                }
                val filteredPlayers = filterPlayers(playerCountries)
                viewModelState.update {
                    it.copy(players = playerCountries, filteredPlayers = filteredPlayers)
                }
            }
        }
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
            val countries = countryRepository.getCountries()
            teamRepository.observeCoaches().collect { coaches ->
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

    private fun observeFavoriteFixtures() {
        viewModelScope.launch {
            //pobierannie danych z shared preferences
        }
    }

    private fun refreshFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesLive()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = ExploreError.RemoteError(result.error)
                    )
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
                    it.copy(filteredTeams = filteredTeams, exploreQuery = newQuery)
                }
                is FilterChip.Explore.Venues -> {
                    val filteredVenues = filterVenues(query = newQuery)
                    it.copy(filteredVenues = filteredVenues, exploreQuery = newQuery)
                }
                is FilterChip.Explore.Players -> {
                    val filteredPlayers = filterPlayers(query = newQuery)
                    it.copy(filteredPlayers = filteredPlayers, exploreQuery = newQuery)
                }
                is FilterChip.Explore.Coaches -> {
                    val filteredCoaches = filterCoaches(query = newQuery)
                    it.copy(filteredCoaches = filteredCoaches, exploreQuery = newQuery)
                }
                is FilterChip.Explore.Countries -> {
                    val filteredCountries = filterCountries(query = newQuery)
                    it.copy(filteredCountries = filteredCountries, exploreQuery = newQuery)
                }
            }
        }
    }

    fun changeExploreView(newExploreFilterChip: FilterChip.Explore) {
        viewModelState.update {
            it.copy(exploreFilterChip = newExploreFilterChip)
        }
    }

    private fun filterFixtures(
        fixtureItems: List<FixtureItem> = viewModelState.value.liveFixtures,
        query: String = viewModelState.value.exploreQuery
    ): List<FixtureItem> {
        return fixtureItems.filter {
            it.homeTeam.name.containsQuery(query) || it.awayTeam.name.containsQuery(query)
                    || it.league.name.containsQuery(query)
                    || it.league.countryName.containsQuery(query)
        }
    }

    private fun filterTeams(
        teams: List<TeamCountry> = viewModelState.value.teams,
        query: String = viewModelState.value.exploreQuery
    ): List<TeamCountry> {
        return teams.filter {
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

    private fun filterCountries(
        countries: List<Country> = viewModelState.value.countries,
        query: String = viewModelState.value.exploreQuery
    ): List<Country> {
        return countries.filter { it.name.containsQuery(query) }
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
        players: List<PlayerCountry> = viewModelState.value.players,
        query: String = viewModelState.value.exploreQuery
    ): List<PlayerCountry> {
        return players.filter {
            it.player.name.containsQuery(query) || it.player.nationality.containsQuery(query)
                    || it.player.firstname.containsQuery(query)
                    || it.player.lastname.containsQuery(query)
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = ExploreError.NoError) }
    }
}