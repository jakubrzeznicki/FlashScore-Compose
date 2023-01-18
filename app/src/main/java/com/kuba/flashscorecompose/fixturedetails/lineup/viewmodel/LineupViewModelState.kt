package com.kuba.flashscorecompose.fixturedetails.lineup.viewmodel

import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.fixturedetails.lineup.model.LineupError
import com.kuba.flashscorecompose.fixturedetails.lineup.model.LineupUiState

/**
 * Created by jrzeznicki on 13/01/2023.
 */
data class LineupViewModelState(
    val isLoading: Boolean = false,
    val error: LineupError = LineupError.NoError,
    val homeLineup: Lineup? = null,
    val awayLineup: Lineup? = null
) {
    fun toUiState(): LineupUiState = if (homeLineup != null && awayLineup != null) {
        LineupUiState.HasData(isLoading, error, homeLineup, awayLineup)
    } else {
        LineupUiState.NoData(isLoading, error)
    }
}