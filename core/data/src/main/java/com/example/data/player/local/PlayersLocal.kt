package com.example.data.player.local

import com.example.database.RoomStorage
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 29/01/2023.
 */
class PlayersLocal(private val roomStorage: RoomStorage) : PlayersLocalDataSource {
    override fun observePlayer(playerId: Int): Flow<com.example.database.players.model.PlayerEntity?> {
        return roomStorage.getDatabase().playerDao().observePlayer(playerId)
    }

    override fun observePlayers(teamId: Int, season: Int): Flow<List<com.example.database.players.model.PlayerEntity>> {
        return roomStorage.getDatabase().playerDao().observePlayers(teamId, season)
    }

    override fun observePlayers(): Flow<List<com.example.database.players.model.PlayerEntity>> {
        return roomStorage.getDatabase().playerDao().observePlayers()
    }

    override suspend fun getFavoritePlayers(ids: List<Int>): List<com.example.database.players.model.PlayerEntity> {
        return roomStorage.getDatabase().playerDao().getFavoritePlayers(ids)
    }

    override suspend fun savePlayers(players: List<com.example.database.players.model.PlayerEntity>) {
        roomStorage.getDatabase().playerDao().savePlayers(players)
    }
}
