package com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel

import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistic
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsError
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsUiState
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper

/**
 * Created by jrzeznicki on 12/01/2023.
 */
data class StatisticsViewModelState(
    val isLoading: Boolean = false,
    val error: StatisticsError = StatisticsError.NoError,
    val homeTeam: Team = Team.EMPTY_TEAM,
    val awayTeam: Team = Team.EMPTY_TEAM,
    val statistics: List<Pair<Statistic, Statistic>> = emptyList(),
    val fixtureItemWrappers: List<FixtureItemWrapper> = emptyList()
) {
    fun toUiState(): StatisticsUiState = when {
        statistics.isNotEmpty() && fixtureItemWrappers.isNotEmpty() ->
            StatisticsUiState.HasAllData(
                isLoading,
                error,
                homeTeam,
                awayTeam,
                statistics,
                fixtureItemWrappers
            )
        statistics.isNotEmpty() ->
            StatisticsUiState.HasOnlyStatistics(isLoading, error, homeTeam, awayTeam, statistics)
        fixtureItemWrappers.isNotEmpty() ->
            StatisticsUiState.HasOnlyOtherFixtures(
                isLoading,
                error,
                homeTeam,
                awayTeam,
                fixtureItemWrappers
            )
        else -> StatisticsUiState.NoData(isLoading, error)
    }
}
