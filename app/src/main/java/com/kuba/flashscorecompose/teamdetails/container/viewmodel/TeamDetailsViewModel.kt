package com.kuba.flashscorecompose.teamdetails.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.team.information.TeamDataSource
import com.kuba.flashscorecompose.teamdetails.container.model.TeamDetailsError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 26/01/2023.
 */
class TeamDetailsViewModel(private val teamId: Int, private val teamRepository: TeamDataSource) :
    ViewModel() {

    private val viewModelState = MutableStateFlow(TeamDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        getTeam()
    }

    private fun getTeam() {
        viewModelScope.launch {
            val team = teamRepository.getTeam(teamId)
            viewModelState.update {
                if (team != null) {
                    it.copy(team = team)
                } else {
                    it.copy(error = TeamDetailsError.EmptyTeamDetails)
                }
            }
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = TeamDetailsError.NoError) }
    }
}