package com.example.fixturedetails.lineup.model

import com.example.model.lineup.Lineup

/**
 * Created by jrzeznicki on 13/01/2023.
 */
interface LineupUiState {
    val isLoading: Boolean
    val error: LineupError

    data class HasData(
        override val isLoading: Boolean,
        override val error: LineupError,
        val lineups: List<Lineup>,
        val selectedLineup: Lineup
    ) : LineupUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: LineupError
    ) : LineupUiState
}