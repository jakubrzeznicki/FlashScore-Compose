package com.kuba.flashscorecompose.data.fixtures.fixture.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.LineupDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class LineupsRemote(private val footballApi: FootballApi) : LineupsRemoteDataSource {
    override suspend fun loadLineups(fixtureId: Int): Response<LineupDataDto> =
        footballApi.getFixturesLineups(fixtureId)
}