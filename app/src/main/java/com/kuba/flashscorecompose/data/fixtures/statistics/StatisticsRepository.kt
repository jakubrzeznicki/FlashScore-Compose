package com.kuba.flashscorecompose.data.fixtures.statistics

import com.kuba.flashscorecompose.data.fixtures.statistics.remote.StatisticsRemoteDataSource
import com.kuba.flashscorecompose.data.fixtures.statistics.local.StatisticsLocalDataSource
import com.kuba.flashscorecompose.data.fixtures.statistics.mapper.toStatistics
import com.kuba.flashscorecompose.data.fixtures.statistics.mapper.toStatisticsEntity
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override suspend fun saveStatistics(statistics: List<Statistics>, fixtureId: Int) {
        local.saveStatistics(statistics.mapIndexed { index, statistics ->
            statistics.toStatisticsEntity(
                fixtureId,
                isHome = index == 0
            )
        })
    }

    override suspend fun loadStatistics(fixtureId: Int): RepositoryResult<List<Statistics>> {
        val result = remote.loadStatistics(fixtureId = fixtureId)
        return try {
            val statistics = result.body()?.response?.mapIndexed { index, statisticsTeamDto ->
                statisticsTeamDto.toStatistics(fixtureId, index == 0)
            }.orEmpty()
            saveStatistics(statistics = statistics, fixtureId = fixtureId)
            RepositoryResult.Success(statistics)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}