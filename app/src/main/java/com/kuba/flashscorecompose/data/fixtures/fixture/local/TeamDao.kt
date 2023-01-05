package com.kuba.flashscorecompose.data.fixtures.fixture.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.TeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.VenueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface TeamDao {

    @Query("SELECT * FROM team WHERE league_id = :leagueId")
    fun getTeamsByLeague(leagueId: Int): Flow<List<TeamEntity>>

    @Query("SELECT * FROM team WHERE team_id = :teamId")
    fun getTeam(teamId: Int): TeamEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTeam(team: TeamEntity)
}