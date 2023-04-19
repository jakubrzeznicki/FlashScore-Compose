package com.example.data.player.local

import com.example.database.RoomStorage
import com.example.database.players.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 29/01/2023.
 */
class PlayersLocal(private val roomStorage: RoomStorage) : PlayersLocalDataSource {
    override fun observePlayer(playerId: Int): Flow<PlayerEntity?> {
        return roomStorage.getDatabase().playerDao().observePlayer(playerId)
    }

    override fun observePlayers(teamIds: List<Int>, season: Int): Flow<List<PlayerEntity>> {
        return roomStorage.getDatabase().playerDao().observePlayers(teamIds, season)
    }

    override fun observePlayers(): Flow<List<PlayerEntity>> {
        return roomStorage.getDatabase().playerDao().observePlayers()
    }

    override suspend fun savePlayers(players: List<PlayerEntity>) {
        roomStorage.getDatabase().playerDao().savePlayers(players)
    }
}
