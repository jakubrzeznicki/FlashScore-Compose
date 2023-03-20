package com.kuba.flashscorecompose.fixturedetails.statistics.model

import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistic
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper

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
        val statistics: List<Pair<Statistic, Statistic>>,
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