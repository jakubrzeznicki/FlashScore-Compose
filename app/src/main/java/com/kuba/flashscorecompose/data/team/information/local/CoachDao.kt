package com.kuba.flashscorecompose.data.team.information.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.team.information.local.model.CoachEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 12/01/2023.
 */
@Dao
interface CoachDao {

    @Query("SELECT * FROM coach WHERE id = :coachId")
    fun observeCoach(coachId: Int): Flow<CoachEntity>

    @Query("SELECT * FROM coach WHERE team_id = :teamId")
    fun observeCoachByTeam(teamId: Int): Flow<CoachEntity?>

    @Query("SELECT * FROM coach WHERE team_id = :teamId")
    suspend fun getCoachByTeam(teamId: Int): CoachEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoaches(coaches: List<CoachEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoach(coach: CoachEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun deleteCoaches(coaches: List<CoachEntity>)
}