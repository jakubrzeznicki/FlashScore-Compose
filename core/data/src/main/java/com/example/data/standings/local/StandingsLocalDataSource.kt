package com.example.data.standings.local

import com.example.database.leagues.model.LeagueEntity
import com.example.database.standings.model.StandingsEntity
import com.example.database.teams.model.TeamEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.Team
 */
interface StandingsLocalDataSource {
    fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<StandingsEntity>>
    fun observeStanding(leagueId: Int, season: Int): Flow<StandingsEntity?>
    suspend fun saveStandings(standings: List<StandingsEntity>)
    suspend fun saveLeagues(leagues: List<LeagueEntity>)
    suspend fun saveTeams(teams: List<TeamEntity>)
}
