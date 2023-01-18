package com.kuba.flashscorecompose.data.fixtures.lineups.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.LineupEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Dao
interface LineupDao {
    @Query("SELECT * FROM lineup WHERE fixture_id = :fixtureId")
    fun observeLineups(fixtureId: Int): Flow<List<LineupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLineups(lineups: List<LineupEntity>)

    @Query("DELETE FROM lineup WHERE fixture_id = :fixtureId")
    fun deleteLineups(fixtureId: Int)
}