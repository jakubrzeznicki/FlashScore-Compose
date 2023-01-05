package com.kuba.flashscorecompose.data.fixtures.fixture.remote

import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.FixtureDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface FixtureRemoteDataSource {
    suspend fun loadFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Response<FixtureDataDto>

    suspend fun loadFixturesHeadToHead(h2h: String): Response<FixtureDataDto>
}