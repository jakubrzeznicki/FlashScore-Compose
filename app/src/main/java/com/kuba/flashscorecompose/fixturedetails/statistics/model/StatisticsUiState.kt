package com.kuba.flashscorecompose.fixturedetails.statistics.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics

/**
 * Created by jrzeznicki on 12/01/2023.
 */
interface StatisticsUiState {
    val isLoading: Boolean
    val error: StatisticsError

    data class HasData(
        override val isLoading: Boolean,
        override val error: StatisticsError,
        val statistics: List<Statistics>,
        val fixtures: List<FixtureItem>
    ) : StatisticsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: StatisticsError
    ) : StatisticsUiState
}