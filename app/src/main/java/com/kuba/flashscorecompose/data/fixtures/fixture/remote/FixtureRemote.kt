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

    override suspend fun loadFixturesHeadToHead(h2h: String, count: Int): Response<FixtureDataDto> =
        footballApi.getFixturesHeadToHead(h2h = h2h, count = count)

    override suspend fun loadFixturesByDate(date: String): Response<FixtureDataDto> =
        footballApi.getFixturesByDate(date)

    override suspend fun loadLastXFixtures(count: Int): Response<FixtureDataDto> =
        footballApi.getLastXFixtures(count = count)

    override suspend fun loadFixturesByTeam(
        teamId: Int,
        season: Int,
        count: Int
    ): Response<FixtureDataDto> = footballApi.getFixturesByTeam(teamId, season, count)
}