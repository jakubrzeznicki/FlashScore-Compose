package com.kuba.flashscorecompose.data.players

import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 29/01/2023.
 */
interface PlayersDataSource {
    fun observePlayer(playerId: Int): Flow<Player>
    fun observePlayers(teamId: Int): Flow<List<Player>>
    suspend fun savePlayers(players: List<Player>)
    suspend fun loadPlayers(teamId: Int, season: Int): RepositoryResult<List<Player>>
}