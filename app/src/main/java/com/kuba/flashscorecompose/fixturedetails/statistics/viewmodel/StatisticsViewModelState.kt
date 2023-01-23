package com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistic
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsError
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsUiState

/**
 * Created by jrzeznicki on 12/01/2023.
 */
data class StatisticsViewModelState(
    val isLoading: Boolean = false,
    val error: StatisticsError = StatisticsError.NoError,
    val homeTeam: Team = Team.EMPTY_TEAM,
    val awayTeam: Team = Team.EMPTY_TEAM,
    val statistics: List<Pair<Statistic, Statistic>> = emptyList(),
    val fixtures: List<FixtureItem> = emptyList()
) {
    fun toUiState(): StatisticsUiState = if (statistics.isNotEmpty() || fixtures.isNotEmpty()) {
        StatisticsUiState.HasData(isLoading, error, homeTeam, awayTeam, statistics, fixtures)
    } else {
        StatisticsUiState.NoData(isLoading, error)
    }
}