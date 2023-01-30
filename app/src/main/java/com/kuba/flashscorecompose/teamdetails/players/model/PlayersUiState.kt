package com.kuba.flashscorecompose.teamdetails.players.model

/**
 * Created by jrzeznicki on 29/01/2023.
 */
interface PlayersUiState {
    val isLoading: Boolean
    val error: PlayersError

    data class HasData(
        override val isLoading: Boolean,
        override val error: PlayersError,
        val playerCountries: List<PlayerCountry>
    ) : PlayersUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: PlayersError
    ) : PlayersUiState
}