package com.kuba.flashscorecompose.teamdetails.players.viewmodel

import com.kuba.flashscorecompose.teamdetails.players.model.PlayerCountry
import com.kuba.flashscorecompose.teamdetails.players.model.PlayersError
import com.kuba.flashscorecompose.teamdetails.players.model.PlayersUiState

/**
 * Created by jrzeznicki on 29/01/2023.
 */
data class PlayersViewModelState(
    val isLoading: Boolean = false,
    val error: PlayersError = PlayersError.NoError,
    val playerCountries: List<PlayerCountry> = emptyList()
) {
    fun toUiState(): PlayersUiState = if (playerCountries.isNotEmpty()) {
        PlayersUiState.HasData(isLoading, error, playerCountries)
    } else {
        PlayersUiState.NoData(isLoading, error)
    }
}