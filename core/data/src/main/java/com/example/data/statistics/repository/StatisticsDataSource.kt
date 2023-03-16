package com.example.data.statistics.repository

import com.example.common.utils.RepositoryResult
import com.example.model.statistics.Statistics
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface StatisticsDataSource {
    fun observeStatistics(fixtureId: Int): Flow<List<Statistics>>
    suspend fun saveStatistics(statistics: List<Statistics>, fixtureId: Int)
    suspend fun loadStatistics(fixtureId: Int): RepositoryResult<List<Statistics>>
}
