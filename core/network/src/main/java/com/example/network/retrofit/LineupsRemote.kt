package com.example.network.retrofit

import com.example.network.LineupsRemoteDataSource
import com.example.network.api.FootballApi
import com.example.network.model.lineup.LineupDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class LineupsRemote(private val footballApi: FootballApi) : LineupsRemoteDataSource {
    override suspend fun loadLineups(fixtureId: Int): Response<LineupDataDto> =
        footballApi.getFixturesLineups(fixtureId)
}
