package com.kuba.flashscorecompose.teamdetails.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.team.information.TeamDataSource
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
            viewModelState.update { it.copy(team = team) }
        }
    }
}