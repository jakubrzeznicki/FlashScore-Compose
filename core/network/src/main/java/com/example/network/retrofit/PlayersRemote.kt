package com.example.network.retrofit

import com.example.network.PlayersRemoteDataSource
import com.example.network.api.FootballApi
import com.example.network.model.player.PlayersDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 28/01/2023.
 */
class PlayersRemote(private val footballApi: FootballApi) : PlayersRemoteDataSource {
    override suspend fun loadPlayers(teamId: Int, season: Int): Response<PlayersDataDto> {
        val queryMap = mapOf(
            FootballApi.TEAM to teamId.toString(),
            FootballApi.SEASON to season.toString()
        )
        return footballApi.getPlayers(queryMap)
    }

    override suspend fun loadPlayer(id: Int, season: Int): Response<PlayersDataDto> {
        val queryMap = mapOf(
            FootballApi.ID to id.toString(),
            FootballApi.SEASON to season.toString()
        )
        return footballApi.getPlayers(queryMap)
    }
}
