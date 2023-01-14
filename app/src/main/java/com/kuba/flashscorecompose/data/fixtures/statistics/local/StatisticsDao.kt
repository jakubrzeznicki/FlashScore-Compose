package com.kuba.flashscorecompose.data.fixtures.statistics.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.fixtures.statistics.local.model.StatisticsEntity
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

    @Query("DELETE FROM statistics WHERE fixture_id = :fixtureId")
    fun deleteStatistics(fixtureId: Int)
}