package com.kuba.flashscorecompose.data.players.remote

import com.kuba.flashscorecompose.data.players.remote.model.PlayersDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 28/01/2023.
 */
interface PlayersRemoteDataSource {
    suspend fun loadPlayers(teamId: Int, season: Int): Response<PlayersDataDto>
    suspend fun loadPlayer(id: Int, season: Int): Response<PlayersDataDto>
}
