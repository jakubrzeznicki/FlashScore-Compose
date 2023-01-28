package com.kuba.flashscorecompose.data.fixtures.lineups.local

import com.kuba.flashscorecompose.data.team.information.local.model.TeamEntity
import com.kuba.flashscorecompose.data.team.information.local.model.CoachEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.LineupEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
interface LineupLocalDataSource {
    fun observeLineups(fixtureId: Int): Flow<List<LineupEntity>>
    suspend fun saveLineups(lineups: List<LineupEntity>)
    suspend fun saveCoaches(coaches: List<CoachEntity>)
    suspend fun saveTeams(teams: List<TeamEntity>)
    suspend fun savePlayers(players: List<PlayerEntity>)
    fun deleteLineups(fixtureId: Int)
}