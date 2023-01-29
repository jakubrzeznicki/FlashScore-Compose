package com.kuba.flashscorecompose.data.players.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.players.remote.model.PlayersDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 28/01/2023.
 */
class PlayersRemote(private val footballApi: FootballApi) : PlayersRemoteDataSource {
    override suspend fun loadPlayers(teamId: Int, season: Int): Response<PlayersDataDto> =
        footballApi.getPlayersByTeam(teamId, season)
}