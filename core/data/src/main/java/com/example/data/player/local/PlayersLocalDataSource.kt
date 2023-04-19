package com.example.data.player.local

import com.example.database.players.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 29/01/2023.
 */
interface PlayersLocalDataSource {
    fun observePlayer(playerId: Int): Flow<PlayerEntity?>
    fun observePlayers(teamIds: List<Int>, season: Int): Flow<List<PlayerEntity>>
    fun observePlayers(): Flow<List<PlayerEntity>>
    suspend fun savePlayers(players: List<PlayerEntity>)
}
