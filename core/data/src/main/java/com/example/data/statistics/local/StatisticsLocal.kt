package com.example.data.statistics.local

import com.example.database.RoomStorage
import com.example.database.fixtures.statistics.model.StatisticsEntity
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
}
