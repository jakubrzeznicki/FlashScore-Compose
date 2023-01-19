package com.kuba.flashscorecompose.standings.model

import com.kuba.flashscorecompose.data.standings.model.Standings

/**
 * Created by jrzeznicki on 18/01/2023.
 */
sealed interface StandingsUiState {
    val isLoading: Boolean
    val error: StandingsError

    data class HasData(
        override val isLoading: Boolean,
        override val error: StandingsError,
        val standings: List<Standings>
    ) : StandingsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: StandingsError
    ) : StandingsUiState
}