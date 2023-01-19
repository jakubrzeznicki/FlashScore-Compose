package com.kuba.flashscorecompose.standings.viewmodel

import com.kuba.flashscorecompose.data.standings.model.Standings
import com.kuba.flashscorecompose.standings.model.StandingsError
import com.kuba.flashscorecompose.standings.model.StandingsUiState

/**
 * Created by jrzeznicki on 18/01/2023.
 */
data class StandingsViewModelState(
    val isLoading: Boolean = false,
    val error: StandingsError = StandingsError.NoError,
    val standings: List<Standings> = emptyList()
) {
    fun toUiState(): StandingsUiState = if (standings != null) {
        StandingsUiState.HasData(isLoading, error, standings)
    } else {
        StandingsUiState.NoData(isLoading, error)
    }
}