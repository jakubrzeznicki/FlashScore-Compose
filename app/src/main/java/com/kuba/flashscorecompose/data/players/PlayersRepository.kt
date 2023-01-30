package com.kuba.flashscorecompose.data.players

import com.kuba.flashscorecompose.data.players.local.PlayersLocalDataSource
import com.kuba.flashscorecompose.data.players.mapper.toPlayer
import com.kuba.flashscorecompose.data.players.mapper.toPlayerEntity
import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.data.players.remote.PlayersRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 29/01/2023.
 */
class PlayersRepository(
    private val local: PlayersLocalDataSource,
    private val remote: PlayersRemoteDataSource
) : PlayersDataSource {
    override fun observePlayer(playerId: Int): Flow<Player> {
        return local.observePlayer(playerId).map { it.toPlayer() }
    }

    override fun observePlayers(teamId: Int, season: Int): Flow<List<Player>> {
        return local.observePlayers(teamId, season).map { playersEntity ->
            playersEntity.map { it.toPlayer() }
        }
    }

    override suspend fun savePlayers(players: List<Player>) {
        local.savePlayers(players.map { it.toPlayerEntity() })
    }

    override suspend fun loadPlayers(teamId: Int, season: Int): RepositoryResult<List<Player>> {
        val result = remote.loadPlayers(teamId, season)
        return try {
            val players = result.body()?.response?.map { playerWrapperDto ->
                playerWrapperDto.player.toPlayer(teamId, season)
            }.orEmpty()
            withContext(Dispatchers.IO) {
                savePlayers(players)
            }
            RepositoryResult.Success(players)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message()
                internalStatus = e.code()
            })
        }
    }
}