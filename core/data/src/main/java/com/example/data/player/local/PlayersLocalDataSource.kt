package com.example.data.player.local

import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 29/01/2023.
 */
interface PlayersLocalDataSource {
    fun observePlayer(playerId: Int): Flow<com.example.database.players.model.PlayerEntity?>
    fun observePlayers(teamId: Int, season: Int): Flow<List<com.example.database.players.model.PlayerEntity>>
    fun observePlayers(): Flow<List<com.example.database.players.model.PlayerEntity>>
    suspend fun getFavoritePlayers(ids: List<Int>): List<com.example.database.players.model.PlayerEntity>
    suspend fun savePlayers(players: List<com.example.database.players.model.PlayerEntity>)
}
