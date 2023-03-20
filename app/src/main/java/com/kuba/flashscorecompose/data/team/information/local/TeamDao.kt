package com.kuba.flashscorecompose.data.team.information.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.team.information.local.model.TeamEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface TeamDao {

    @Query("SELECT * FROM team WHERE league_id = :leagueId")
    fun observeTeamsByLeague(leagueId: Int): Flow<List<TeamEntity>>

    @Query("SELECT * FROM team WHERE id = :teamId")
    suspend fun getTeam(teamId: Int): TeamEntity?

    @Query("SELECT * FROM team WHERE id = :teamId")
    fun observeTeam(teamId: Int): Flow<TeamEntity?>

    @Query("SELECT * FROM team")
    fun observeTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM team WHERE id IN(:ids)")
    fun observeFavoriteTeams(ids: List<Int>): Flow<List<TeamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTeam(team: TeamEntity)
}