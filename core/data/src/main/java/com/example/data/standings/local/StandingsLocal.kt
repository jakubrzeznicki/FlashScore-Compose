package com.example.data.standings.local

import com.example.database.RoomStorage
import com.example.database.leagues.model.LeagueEntity
import com.example.database.standings.model.StandingsEntity
import com.example.database.teams.model.TeamEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsLocal(private val roomStorage: RoomStorage) : StandingsLocalDataSource {
    override fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<StandingsEntity>> {
        return roomStorage.getDatabase().standingDao().observeStandings(leagueIds, season)
    }

    override fun observeStanding(leagueId: Int, season: Int): Flow<StandingsEntity?> {
        return roomStorage.getDatabase().standingDao().observeStanding(leagueId, season)
    }

    override suspend fun saveStandings(standings: List<StandingsEntity>) {
        roomStorage.getDatabase().standingDao().saveStandings(standings)
    }

    override suspend fun saveLeagues(leagues: List<LeagueEntity>) {
        roomStorage.getDatabase().leagueDao().saveLeagues(leagues)
    }

    override suspend fun saveTeams(teams: List<TeamEntity>) {
        roomStorage.getDatabase().teamDao().saveTeams(teams)
    }
}
