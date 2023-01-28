package com.kuba.flashscorecompose.data.standings.local

import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import com.kuba.flashscorecompose.data.team.information.local.model.TeamEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.Team
 */
interface StandingsLocalDataSource {
    fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<StandingsEntity>>
    suspend fun getStanding(leagueId: Int, season: Int): StandingsEntity
    suspend fun saveStandings(standings: List<StandingsEntity>)
    suspend fun saveLeagues(leagues: List<LeagueEntity>)
    suspend fun saveTeams(teams: List<TeamEntity>)
}