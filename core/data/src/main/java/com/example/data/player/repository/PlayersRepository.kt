package com.example.data.player.repository

import com.example.common.utils.RepositoryResult
import com.example.common.utils.ResponseStatus
import com.example.data.player.local.PlayersLocalDataSource
import com.example.data.player.mapper.toPlayer
import com.example.data.player.mapper.toPlayerEntity
import com.example.database.players.mapper.toPlayer
import com.example.model.player.Player
import com.example.model.team.Team
import com.example.network.PlayersRemoteDataSource
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
    override fun observePlayer(playerId: Int): Flow<Player?> {
        return local.observePlayer(playerId).map { it?.toPlayer() }
    }

    override fun observePlayers(teamId: Int, season: Int): Flow<List<Player>> {
        return local.observePlayers(teamId, season).map { playerEntities ->
            playerEntities.map { it.toPlayer() }
        }
    }

    override fun observePlayers(): Flow<List<Player>> {
        return local.observePlayers().map { playersEntities ->
            playersEntities.map { it.toPlayer() }
        }
    }

    override suspend fun savePlayers(players: List<Player>) {
        local.savePlayers(players.map { it.toPlayerEntity() })
    }

    override suspend fun loadPlayers(team: Team, season: Int): RepositoryResult<List<Player>> {
        val result = remote.loadPlayers(team.id, season)
        return try {
            val players = result.body()?.response?.map { playerWrapperDto ->
                playerWrapperDto.player?.toPlayer(team, season) ?: Player.EMPTY_PLAYER
            }.orEmpty()
            withContext(Dispatchers.IO) {
                savePlayers(players)
            }
            RepositoryResult.Success(players)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message()
                    internalStatus = e.code()
                }
            )
        }
    }

    override suspend fun loadPlayer(
        id: Int,
        team: Team,
        season: Int
    ): RepositoryResult<List<Player>> {
        val result = remote.loadPlayer(id, season)
        return try {
            val players = result.body()?.response?.map { playerWrapperDto ->
                playerWrapperDto.player?.toPlayer(team, season) ?: Player.EMPTY_PLAYER
            }.orEmpty()
            withContext(Dispatchers.IO) {
                savePlayers(players)
            }
            RepositoryResult.Success(players)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message()
                    internalStatus = e.code()
                }
            )
        }
    }
}
