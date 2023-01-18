package com.kuba.flashscorecompose.data.fixtures.statistics.local

import com.kuba.flashscorecompose.data.fixtures.statistics.local.model.StatisticsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
interface StatisticsLocalDataSource {
    fun observeStatistics(fixtureId: Int): Flow<List<StatisticsEntity>>
    suspend fun saveStatistics(statistics: List<StatisticsEntity>)
    fun deleteStatistics(fixtureId: Int)
}