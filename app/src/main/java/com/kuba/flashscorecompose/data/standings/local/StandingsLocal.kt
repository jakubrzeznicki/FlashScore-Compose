package com.kuba.flashscorecompose.data.standings.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import com.kuba.flashscorecompose.data.team.information.local.model.TeamEntity
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
