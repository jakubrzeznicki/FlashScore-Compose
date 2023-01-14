package com.kuba.flashscorecompose.data.fixtures.statistics

import android.util.Log
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toLineup
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
            Log.d("TEST_LOG", "observeStatistics repo $statisticsEntity")
            Log.d(
                "TEST_LOG",
                "observeStatistics repo converted ${statisticsEntity.map { it.toStatistics() }}"
            )
            statisticsEntity.map { it.toStatistics() }
        }
    }

    override suspend fun saveStatistics(statistics: List<Statistics>) {
        local.saveStatistics(statistics.map { it.toStatisticsEntity() })
    }

    override suspend fun loadStatistics(fixtureId: Int): RepositoryResult<List<Statistics>> {
        val result = remote.loadStatistics(fixtureId = fixtureId)
        Log.d("TEST_LOG", "loadStatiustucs in repo - ${result.code()}")
        Log.d("TEST_LOG", "loadStatiustucs in repo - ${result.message()}")
        Log.d("TEST_LOG", "loadStatiustucs in repo - ${result.body()}")
        return try {
            val statistics = result.body()?.response?.map { it.toStatistics(fixtureId) }.orEmpty()
            Log.d("TEST_LOG", "loadStatistics convertertet $statistics")
            local.saveStatistics(statistics.map { it.toStatisticsEntity() })
            Log.d(
                "TEST_LOG",
                "loadStatistics convertertet to entity ${statistics.map { it.toStatisticsEntity() }}"
            )
            RepositoryResult.Success(statistics)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}