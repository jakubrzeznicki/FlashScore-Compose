package com.kuba.flashscorecompose.data.fixtures.currentround.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface CurrentRoundDao {
    @Query("SELECT * FROM current_rounds WHERE league_id = :leagueId")
    fun getCurrentRound(leagueId: Int): CurrentRoundEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrentRound(currentRound: CurrentRoundEntity)

    @Query("DELETE FROM current_rounds WHERE league_id = :leagueId")
    fun deleteCurrentRound(leagueId: Int)
}