package com.kuba.flashscorecompose.data.team.information.local

import androidx.room.*
import com.kuba.flashscorecompose.data.team.information.local.model.CoachEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 12/01/2023.
 */
@Dao
interface CoachDao {

    @Query("SELECT * FROM coach WHERE id = :coachId")
    fun observeCoach(coachId: Int): Flow<CoachEntity?>

    @Query("SELECT * FROM coach")
    fun observeCoaches(): Flow<List<CoachEntity>>

    @Query("SELECT * FROM coach WHERE team_id = :teamId")
    fun observeCoachByTeam(teamId: Int): Flow<CoachEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoaches(coaches: List<CoachEntity>): List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateCoaches(coaches: List<CoachEntity>)

    @Transaction
    suspend fun upsertCoaches(coaches: List<CoachEntity>) {
        val ids: List<Long> = insertCoaches(coaches)
        val updatedCoaches = mutableListOf<CoachEntity>()
        ids.forEachIndexed { index, id ->
            if (id == -1L) updatedCoaches.add(coaches[index])
        }
        if (updatedCoaches.isNotEmpty()) updateCoaches(updatedCoaches)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoach(coach: CoachEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateCoach(coach: CoachEntity)

    @Transaction
    suspend fun upsertCoach(coach: CoachEntity) {
        val id: Long = insertCoach(coach)
        if (id == -1L) updateCoach(coach)
    }
}
