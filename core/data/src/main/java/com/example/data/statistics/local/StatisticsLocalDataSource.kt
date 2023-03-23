package com.example.data.statistics.local

import com.example.database.fixtures.statistics.model.StatisticsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
interface StatisticsLocalDataSource {
    fun observeStatistics(fixtureId: Int): Flow<List<StatisticsEntity>>
    suspend fun saveStatistics(statistics: List<StatisticsEntity>)
}
