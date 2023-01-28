package com.kuba.flashscorecompose.data.fixtures.lineups.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.team.information.local.model.TeamEntity
import com.kuba.flashscorecompose.data.team.information.local.model.CoachEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.LineupEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
class LineupLocal(private val roomStorage: RoomStorage) : LineupLocalDataSource {
    override fun observeLineups(fixtureId: Int): Flow<List<LineupEntity>> {
        return roomStorage.getDatabase().lineupDao().observeLineups(fixtureId)
    }

    override suspend fun saveLineups(lineups: List<LineupEntity>) {
        roomStorage.getDatabase().lineupDao().saveLineups(lineups)
    }

    override suspend fun saveCoaches(coaches: List<CoachEntity>) {
        roomStorage.getDatabase().coachDao().saveCoaches(coaches)
    }

    override suspend fun saveTeams(teams: List<TeamEntity>) {
        roomStorage.getDatabase().teamDao().saveTeams(teams)
    }

    override suspend fun savePlayers(players: List<PlayerEntity>) {
        roomStorage.getDatabase().playerDao().savePlayers(players)
    }

    override fun deleteLineups(fixtureId: Int) {
        roomStorage.getDatabase().lineupDao().deleteLineups(fixtureId)
    }
}