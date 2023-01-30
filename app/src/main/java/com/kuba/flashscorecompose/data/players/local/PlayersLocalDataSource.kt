package com.kuba.flashscorecompose.data.players.local

import com.kuba.flashscorecompose.data.players.local.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 29/01/2023.
 */
interface PlayersLocalDataSource {
    fun observePlayer(playerId: Int): Flow<PlayerEntity>
    fun observePlayers(teamId: Int, season: Int): Flow<List<PlayerEntity>>
    suspend fun savePlayers(players: List<PlayerEntity>)
}