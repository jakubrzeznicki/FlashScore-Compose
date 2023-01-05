package com.kuba.flashscorecompose.data.fixtures.fixture.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.FixtureDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class FixtureRemote(private val footballApi: FootballApi) : FixtureRemoteDataSource {
    override suspend fun loadFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Response<FixtureDataDto> =
        footballApi.getFixturesFilteredByRound(
            leagueId = leagueId,
            season = season,
            round = round
        )

    override suspend fun loadFixturesHeadToHead(h2h: String): Response<FixtureDataDto> =
        footballApi.getFixturesHeadToHead(h2h = h2h)
}