package com.example.fixturedetails.statistics.viewmodel

import com.example.fixturedetails.statistics.model.StatisticsError
import com.example.fixturedetails.statistics.model.StatisticsUiState
import com.example.model.fixture.FixtureItemWrapper
import com.example.model.statistics.Statistic
import com.example.model.team.Team

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
