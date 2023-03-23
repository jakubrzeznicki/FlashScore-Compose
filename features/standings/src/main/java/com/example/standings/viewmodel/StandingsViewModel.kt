package com.example.standings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.RepositoryResult
import com.example.common.utils.containsQuery
import com.example.data.country.repository.CountryDataSource
import com.example.data.league.repository.LeagueDataSource
import com.example.data.standings.repository.StandingsDataSource
import com.example.model.country.Country
import com.example.model.league.League
import com.example.model.standings.Standing
import com.example.standings.model.StandingsError
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsViewModel(
    private val countryRepository: CountryDataSource,
    private val leagueRepository: LeagueDataSource,
    private val standingsRepository: StandingsDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(StandingsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observeCountries()
        observeStandings()
//        refreshCountries()
//        refreshStandings()
    }

    fun refresh() {
        refreshCountries()
        refreshStandings()
    }

    private fun observeCountries() {
        viewModelScope.launch {
            countryRepository.observeCountries(COUNTRY_NAMES).collect { countries ->
                viewModelState.update {
                    it.copy(countries = countries)
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
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            isLoading = false,
                            error = StandingsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    private fun refreshStandings() {
        viewModelScope.launch {
            val leagues = leagueRepository.getLeagues(COUNTRY_NAMES)
            if (leagues.isEmpty()) {
                viewModelState.update { it.copy(error = StandingsError.EmptyLeague) }
                return@launch
            }
            leagues.filter { league -> LEAGUES.any { league.name == it } }.forEach { league ->
                refreshStanding(league)
            }
        }
    }

    private fun refreshStanding(league: League) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = standingsRepository.loadStandings(league.id, league.season)
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
                            error = StandingsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    private fun observeStandings() {
        viewModelScope.launch {
            val leagues = leagueRepository.getLeagues(COUNTRY_NAMES)
            if (leagues.isEmpty()) {
                viewModelState.update { it.copy(error = StandingsError.EmptyLeague) }
                return@launch
            }
            standingsRepository.observeStandings(leagues.map { it.id }, leagues.first().season)
                .collect { standings ->
                    val filteredStandings = filterStandings(standings)
                    viewModelState.update {
                        it.copy(standings = standings, filteredStandings = filteredStandings)
                    }
                }
        }
    }

    fun updateStandingsQuery(newQuery: String) {
        val filteredStandings = filterStandings(query = newQuery)
        viewModelState.update {
            it.copy(filteredStandings = filteredStandings, standingsQuery = newQuery)
        }
    }

    fun updateSelectedCountry(newSelectedCountry: Country, isSelected: Boolean) {
        val filteredStandings = if (isSelected) {
            viewModelState.value.standings
        } else {
            filterStandings(selectedCountry = newSelectedCountry)
        }
        viewModelState.update {
            it.copy(
                filteredStandings = filteredStandings,
                selectedCountry = if (isSelected) Country.EMPTY_COUNTRY else newSelectedCountry,
                standingsQuery = if (isSelected) EMPTY else viewModelState.value.standingsQuery
            )
        }
    }

    private fun filterStandings(
        standings: List<Standing> = viewModelState.value.standings,
        query: String = viewModelState.value.standingsQuery,
        selectedCountry: Country = viewModelState.value.selectedCountry
    ): List<Standing> {
        val countryName = selectedCountry.name
        val countryCode = selectedCountry.code
        return standings
            .filter {
                (it.league.name.containsQuery(query) || it.league.countryName.containsQuery(query)) &&
                        (it.league.countryName == countryName || it.league.countryCode == countryCode)
            }
    }

    private companion object {
        const val EMPTY = ""
        val COUNTRY_NAMES = listOf(
            "Poland",
            "Germany",
            "France",
            "Spain",
            "Netherlands",
            "Portugal",
            "Turkey",
            "Italy",
            "England"
        )
        val LEAGUES = listOf(
            "Premier League",
            "Championship",
            "Ligue 1",
            "Ligue 2",
            "Eredivisie",
            "Ekstraklasa",
            "I Liga",
            "Primeira Liga",
            "La Liga",
            "Bundesliga",
            "2. Bundesliga",
            "Serie A",
            "Serie B"
        )
    }
}
