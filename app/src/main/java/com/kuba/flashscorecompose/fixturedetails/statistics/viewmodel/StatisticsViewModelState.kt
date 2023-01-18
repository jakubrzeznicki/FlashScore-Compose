package com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsError
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsUiState

/**
 * Created by jrzeznicki on 12/01/2023.
 */
data class StatisticsViewModelState(
    val isLoading: Boolean = false,
    val error: StatisticsError = StatisticsError.NoError,
    val homeStatistics: Statistics? = null,
    val awayStatistics: Statistics? = null,
    val fixtures: List<FixtureItem> = emptyList()
) {
    fun toUiState(): StatisticsUiState = if (homeStatistics != null && awayStatistics != null) {
        StatisticsUiState.HasData(isLoading, error, homeStatistics, awayStatistics, fixtures)
    } else {
        StatisticsUiState.NoData(isLoading, error)
    }
}