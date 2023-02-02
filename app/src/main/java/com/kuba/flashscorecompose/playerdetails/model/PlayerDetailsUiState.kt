package com.kuba.flashscorecompose.playerdetails.model

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.players.model.Player

/**
 * Created by jrzeznicki on 01/02/2023.
 */
interface PlayerDetailsUiState {
    val error: PlayerDetailsError

    data class HasData(
        override val error: PlayerDetailsError,
        val country: Country?,
        val player: Player
    ) : PlayerDetailsUiState

    data class NoData(
        override val error: PlayerDetailsError,
    ) : PlayerDetailsUiState
}