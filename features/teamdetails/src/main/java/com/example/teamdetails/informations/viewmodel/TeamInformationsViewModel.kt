package com.example.teamdetails.informations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.RepositoryResult
import com.example.data.country.repository.CountryDataSource
import com.example.data.team.repository.TeamDataSource
import com.example.model.country.Country
import com.example.model.team.Team
import com.example.teamdetails.informations.model.TeamInformationsError
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 27/01/2023.
 */
class TeamInformationsViewModel(
    private val team: Team,
    private val leagueId: Int,
    private val season: Int,
    private val teamRepository: TeamDataSource,
    private val countryRepository: CountryDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    private val viewModelState = MutableStateFlow(TeamInformationsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observeTeam()
        observeCoach()
        observeVenue()
    }

    fun refresh() {
        refreshTeamInformation()
        refreshCoach()
    }

    private fun observeTeam() {
        viewModelScope.launch {
            teamRepository.observeTeam(team.id).collect { team ->
                if (team == null) {
                    viewModelState.update { it.copy(error = TeamInformationsError.EmptyTeam) }
                    return@collect
                }
                val country = countryRepository.getCountry(team.country)
                viewModelState.update {
                    it.copy(team = team, country = country ?: Country.EMPTY_COUNTRY)
                }
            }
        }
    }

    private fun observeCoach() {
        viewModelScope.launch {
            teamRepository.observeCoach(team.id).collect { coach ->
                viewModelState.update {
                    it.copy(coach = coach)
                }
            }
        }
    }

    private fun observeVenue() {
        viewModelScope.launch {
            teamRepository.observeVenue(team.id).collect { venue ->
                viewModelState.update {
                    it.copy(venue = venue)
                }
            }
        }
    }

    private fun refreshTeamInformation() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = teamRepository.loadTeamInformation(team.id, leagueId, season)
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
                            error = TeamInformationsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    private fun refreshCoach() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = teamRepository.loadCoach(team.id)
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
                            error = TeamInformationsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }
}