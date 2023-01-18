package com.kuba.flashscorecompose.data.fixtures.statistics

import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface StatisticsDataSource {
    fun observeStatistics(fixtureId: Int): Flow<List<Statistics>>
    suspend fun saveStatistics(statistics: List<Statistics>, fixtureId: Int)
    suspend fun loadStatistics(fixtureId: Int): RepositoryResult<List<Statistics>>
}