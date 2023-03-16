package com.example.fixturedetails.statistics.model

import com.example.model.fixture.FixtureItemWrapper
import com.example.model.statistics.Statistic
import com.example.model.team.Team

/**
 * Created by jrzeznicki on 12/01/2023.
 */
interface StatisticsUiState {
    val isLoading: Boolean
    val error: StatisticsError

    data class HasAllData(
        override val isLoading: Boolean,
        override val error: StatisticsError,
        val homeTeam: Team,
        val awayTeam: Team,
        val statistics: List<Pair<Statistic, Statistic>>,
        val fixtureItemWrappers: List<FixtureItemWrapper>
    ) : StatisticsUiState

    data class HasOnlyStatistics(
        override val isLoading: Boolean,
        override val error: StatisticsError,
        val homeTeam: Team,
        val awayTeam: Team,
        val statistics: List<Pair<Statistic, Statistic>>
    ) : StatisticsUiState

    data class HasOnlyOtherFixtures(
        override val isLoading: Boolean,
        override val error: StatisticsError,
        val homeTeam: Team,
        val awayTeam: Team,
        val fixtureItemWrappers: List<FixtureItemWrapper>
    ) : StatisticsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: StatisticsError
    ) : StatisticsUiState
}
