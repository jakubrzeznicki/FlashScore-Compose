package com.kuba.flashscorecompose.standings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.league.LeagueDataSource
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.StandingsDataSource
import com.kuba.flashscorecompose.data.standings.model.Standings
import com.kuba.flashscorecompose.home.viewmodel.HomeViewModel
import com.kuba.flashscorecompose.standings.model.StandingsError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsViewModel(
    private val countryRepository: CountryDataSource,
    private val leagueRepository: LeagueDataSource,
    private val standingsRepository: StandingsDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(StandingsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observeCountries()
        observeStandings()
        //refreshCountries()
        //refreshStandings()
    }

    private fun observeCountries() {
        viewModelScope.launch {
            countryRepository.observeCountries(HomeViewModel.COUNTRY_CODES).collect { countries ->
                viewModelState.update {
                    it.copy(countryItems = countries)
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
                        error = StandingsError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    fun refreshStandings() {
        viewModelScope.launch {
            val leagues = leagueRepository.getLeagues(HomeViewModel.COUNTRY_NAMES)
            leagues.forEach { league ->
                //refreshStanding(league)
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
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = StandingsError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    private fun observeStandings() {
        viewModelScope.launch {
            val leagues = leagueRepository.getLeagues(HomeViewModel.COUNTRY_NAMES)
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

    fun getStandingsByCountry(countryName: String, isSelected: Boolean) {
        val filteredStandings = if (isSelected) {
            viewModelState.value.standings
        } else {
            filterStandings(query = countryName)
        }
        viewModelState.update {
            it.copy(filteredStandings = filteredStandings, standingsQuery = "")
        }
    }

    private fun filterStandings(
        standings: List<Standings> = viewModelState.value.standings,
        query: String = viewModelState.value.standingsQuery
    ): List<Standings> {
        return standings
            .filter {
                it.league.name.containsQuery(query) || it.league.countryName.containsQuery(query)
            }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = StandingsError.NoError) }
    }

    private fun String?.containsQuery(query: String) =
        this.orEmpty()
            .lowercase(Locale.getDefault())
            .contains(query.lowercase(Locale.getDefault()))
}