package com.kuba.flashscorecompose.fixturedetails.statistics.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistic

/**
 * Created by jrzeznicki on 12/01/2023.
 */
interface StatisticsUiState {
    val isLoading: Boolean
    val error: StatisticsError

    data class HasData(
        override val isLoading: Boolean,
        override val error: StatisticsError,
        val homeTeam: Team,
        val awayTeam: Team,
        val statistics: List<Pair<Statistic, Statistic>>,
        val fixtures: List<FixtureItem>
    ) : StatisticsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: StatisticsError
    ) : StatisticsUiState
}