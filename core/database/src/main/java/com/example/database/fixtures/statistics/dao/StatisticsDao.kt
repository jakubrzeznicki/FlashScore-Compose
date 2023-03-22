package com.example.database.fixtures.statistics.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.fixtures.statistics.model.StatisticsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Dao
interface StatisticsDao {

    @Query("SELECT * FROM statistics WHERE fixture_id = :fixtureId")
    fun observeStatistics(fixtureId: Int): Flow<List<StatisticsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStatistics(statistics: List<StatisticsEntity>)
}
