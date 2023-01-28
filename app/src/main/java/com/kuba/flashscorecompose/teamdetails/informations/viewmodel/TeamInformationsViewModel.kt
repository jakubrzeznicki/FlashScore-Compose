package com.kuba.flashscorecompose.teamdetails.informations.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val teamRepository: TeamDataSource
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
                Log.d("TEST_LOG", "observe team - $team")
                viewModelState.update {
                    it.copy(team = team)
                }
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
            val result = teamRepository.loadTeamInformation(teamId, leagueId)
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