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
    val lineups: List<Lineup> = emptyList(),
    val selectedLineup: Lineup = Lineup.EMPTY_LINEUP
) {
    fun toUiState(): LineupUiState = if (lineups.isNotEmpty()) {
        LineupUiState.HasData(
            isLoading,
            error,
            lineups,
            selectedLineup
        )
    } else {
        LineupUiState.NoData(isLoading, error)
    }
}
