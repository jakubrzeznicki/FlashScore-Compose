package com.example.data.statistics.repository

import com.example.common.utils.RepositoryResult
import com.example.common.utils.ResponseStatus
import com.example.data.statistics.local.StatisticsLocalDataSource
import com.example.data.statistics.mapper.toStatistics
import com.example.data.statistics.mapper.toStatisticsEntity
import com.example.database.fixtures.statistics.mapper.toStatistics
import com.example.model.statistics.Statistics
import com.example.network.StatisticsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class StatisticsRepository(
    private val local: StatisticsLocalDataSource,
    private val remote: StatisticsRemoteDataSource
) : StatisticsDataSource {

    override fun observeStatistics(fixtureId: Int): Flow<List<Statistics>> {
        return local.observeStatistics(fixtureId).map { statisticsEntity ->
            statisticsEntity.map { it.toStatistics() }
        }
    }

    override suspend fun saveStatistics(statisticsParam: List<Statistics>, fixtureId: Int) {
        local.saveStatistics(
            statisticsParam.mapIndexed { index, statistics ->
                statistics.toStatisticsEntity(fixtureId, isHome = index == 0)
            }
        )
    }

    override suspend fun loadStatistics(fixtureId: Int): RepositoryResult<List<Statistics>> {
        val result = remote.loadStatistics(fixtureId = fixtureId)
        return try {
            val statistics = result.body()?.response?.map { statisticsTeamDto ->
                statisticsTeamDto.toStatistics(fixtureId)
            }.orEmpty()
            withContext(Dispatchers.IO) {
                saveStatistics(statisticsParam = statistics, fixtureId = fixtureId)
            }
            RepositoryResult.Success(statistics)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }
}
