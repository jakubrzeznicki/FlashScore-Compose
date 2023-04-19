package com.example.database.teams.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.teams.model.TeamEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface TeamDao {

    @Query("SELECT * FROM team WHERE id = :teamId")
    fun observeTeam(teamId: Int): Flow<TeamEntity?>

    @Query("SELECT * FROM team WHERE id != 0")
    fun observeTeams(): Flow<List<TeamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTeam(team: TeamEntity)
}
