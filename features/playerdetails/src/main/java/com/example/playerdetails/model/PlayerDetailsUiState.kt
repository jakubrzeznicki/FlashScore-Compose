package com.example.playerdetails.model

import com.example.model.country.Country
import com.example.model.player.Player

/**
 * Created by jrzeznicki on 01/02/2023.
 */
interface PlayerDetailsUiState {
    val isLoading: Boolean
    val error: PlayerDetailsError

    data class HasData(
        override val isLoading: Boolean,
        override val error: PlayerDetailsError,
        val country: Country?,
        val player: Player
    ) : PlayerDetailsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: PlayerDetailsError
    ) : PlayerDetailsUiState
}
