package com.example.teamdetails.players.viewmodel

import com.example.model.player.PlayerWrapper
import com.example.model.team.Team
import com.example.teamdetails.players.model.PlayersError
import com.example.teamdetails.players.model.PlayersUiState

/**
 * Created by jrzeznicki on 29/01/2023.
 */
data class PlayersViewModelState(
    val isLoading: Boolean = false,
    val error: PlayersError = PlayersError.NoError,
    val team: Team = Team.EMPTY_TEAM,
    val playerWrappers: List<PlayerWrapper> = emptyList()
) {
    fun toUiState(): PlayersUiState = if (playerWrappers.isNotEmpty()) {
        PlayersUiState.HasData(isLoading, error, team, playerWrappers)
    } else {
        PlayersUiState.NoData(isLoading, error)
    }
}
