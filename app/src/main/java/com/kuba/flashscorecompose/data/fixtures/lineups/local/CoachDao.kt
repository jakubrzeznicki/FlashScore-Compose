package com.kuba.flashscorecompose.data.fixtures.lineups.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.CoachEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 12/01/2023.
 */
@Dao
interface CoachDao {

    @Query("SELECT * FROM coach WHERE id = :coachId")
    fun getCoach(coachId: Int): Flow<CoachEntity>

    @Query("SELECT * FROM coach WHERE teamId = :teamId")
    fun getCoachByTeamId(teamId: Int): Flow<CoachEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoaches(coaches: List<CoachEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun deleteCoaches(coaches: List<CoachEntity>)
}