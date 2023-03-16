package com.example.teamdetails.players.model

import com.example.model.player.PlayerWrapper
import com.example.model.team.Team

/**
 * Created by jrzeznicki on 29/01/2023.
 */
interface PlayersUiState {
    val isLoading: Boolean
    val error: PlayersError

    data class HasData(
        override val isLoading: Boolean,
        override val error: PlayersError,
        val team: Team,
        val playerWrappers: List<PlayerWrapper>
    ) : PlayersUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: PlayersError
    ) : PlayersUiState
}
