package com.kuba.flashscorecompose.data.standings.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsLocal(private val roomStorage: RoomStorage) : StandingsLocalDataSource {
    override fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<StandingsEntity>> {
        return roomStorage.getDatabase().standingDao().observeStandings(leagueIds, season)
    }

    override suspend fun getStanding(leagueId: Int, season: Int): StandingsEntity {
        return roomStorage.getDatabase().standingDao().getStanding(leagueId, season)
    }

    override suspend fun saveStandings(standings: List<StandingsEntity>) {
        roomStorage.getDatabase().standingDao().saveStandings(standings)
    }
}