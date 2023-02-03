package com.kuba.flashscorecompose.teamdetails.informations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.team.information.TeamDataSource
import com.kuba.flashscorecompose.teamdetails.informations.model.TeamInformationsError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 27/01/2023.
 */
class TeamInformationsViewModel(
    private val teamId: Int,
    private val leagueId: Int,
    private val season: Int,
    private val teamRepository: TeamDataSource,
    private val countryRepository: CountryDataSource
) : ViewModel() {
    private val viewModelState = MutableStateFlow(TeamInformationsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observeTeam()
        observeCoach()
        observeVenue()
    }


    private fun observeTeam() {
        viewModelScope.launch {
            teamRepository.observeTeam(teamId).collect { team ->
                if (team == null) {
                    viewModelState.update { it.copy(error = TeamInformationsError.EmptyTeam) }
                    return@collect
                }
                observeCountry(team.country)
                viewModelState.update {
                    it.copy(team = team)
                }
            }
        }
    }

    private fun observeCountry(name: String) {
        viewModelScope.launch {
            countryRepository.observeCountries(listOf(name)).collect { countries ->
                viewModelState.update { it.copy(country = countries.firstOrNull()) }
            }
        }
    }

    private fun observeCoach() {
        viewModelScope.launch {
            teamRepository.observeCoach(teamId).collect { coach ->
                viewModelState.update {
                    it.copy(coach = coach)
                }
            }
        }
    }

    private fun observeVenue() {
        viewModelScope.launch {
            teamRepository.observeVenue(teamId).collect { venue ->
                viewModelState.update {
                    it.copy(venue = venue)
                }
            }
        }
    }

    fun refresh() {
        refreshTeamInformation()
        refreshCoach()
    }

    private fun refreshTeamInformation() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = teamRepository.loadTeamInformation(teamId, leagueId, season)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = TeamInformationsError.RemoteError(result.error),
                    )
                }
            }
        }
    }

    private fun refreshCoach() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = teamRepository.loadCoach(teamId)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = TeamInformationsError.RemoteError(result.error),
                    )
                }
            }
        }
    }
}