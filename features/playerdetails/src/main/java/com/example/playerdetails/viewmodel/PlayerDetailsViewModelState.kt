package com.example.playerdetails.viewmodel

import com.example.model.country.Country
import com.example.model.player.Player
import com.example.playerdetails.model.PlayerDetailsError
import com.example.playerdetails.model.PlayerDetailsUiState

/**
 * Created by jrzeznicki on 01/02/2023.
 */
data class PlayerDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: PlayerDetailsError = PlayerDetailsError.NoError,
    val country: Country? = Country.EMPTY_COUNTRY,
    val player: Player? = Player.EMPTY_PLAYER
) {
    fun toUiState(): PlayerDetailsUiState = if (player != null) {
        PlayerDetailsUiState.HasData(isLoading, error, country, player)
    } else {
        PlayerDetailsUiState.NoData(isLoading, error)
    }
}
