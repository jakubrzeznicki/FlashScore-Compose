package com.kuba.flashscorecompose.data.fixtures.statistics.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.fixtures.statistics.local.model.StatisticsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
class StatisticsLocal(private val roomStorage: RoomStorage) : StatisticsLocalDataSource {
    override fun observeStatistics(fixtureId: Int): Flow<List<StatisticsEntity>> {
        return roomStorage.getDatabase().statisticsDao().observeStatistics(fixtureId)
    }

    override suspend fun saveStatistics(statistics: List<StatisticsEntity>) {
        roomStorage.getDatabase().statisticsDao().saveStatistics(statistics)
    }

    override fun deleteStatistics(fixtureId: Int) {
        roomStorage.getDatabase().statisticsDao().deleteStatistics(fixtureId)
    }
}