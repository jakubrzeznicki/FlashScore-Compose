package com.kuba.flashscorecompose.data.players.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.players.local.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 29/01/2023.
 */
class PlayersLocal(private val roomStorage: RoomStorage) : PlayersLocalDataSource {
    override fun observePlayer(playerId: Int): Flow<PlayerEntity> {
        return roomStorage.getDatabase().playerDao().observePlayer(playerId)
    }

    override fun observePlayers(teamId: Int): Flow<List<PlayerEntity>> {
        return roomStorage.getDatabase().playerDao().observePlayers(teamId)
    }

    override suspend fun savePlayers(players: List<PlayerEntity>) {
        roomStorage.getDatabase().playerDao().savePlayers(players)
    }

}