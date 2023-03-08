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
    ): Response<FixtureDataDto> {
        val queryMap = mapOf(
            FootballApi.LEAGUE to leagueId.toString(),
            FootballApi.SEASON to season.toString(),
            FootballApi.ROUND to round
        )
        return footballApi.getFixtures(queryMap)
    }

    override suspend fun loadFixturesHeadToHead(h2h: String, count: Int): Response<FixtureDataDto> =
        footballApi.getFixturesHeadToHead(h2h = h2h, count = count)

    override suspend fun loadFixturesByDate(date: String): Response<FixtureDataDto> {
        val queryMap = mapOf(FootballApi.DATE to date)
        return footballApi.getFixtures(queryMap)
    }

    override suspend fun loadFixturesByDate(
        date: String,
        leagueId: Int,
        season: Int
    ): Response<FixtureDataDto> {
        val queryMap = mapOf(
            FootballApi.LEAGUE to leagueId.toString(),
            FootballApi.SEASON to season.toString(),
            FootballApi.DATE to date
        )
        return footballApi.getFixtures(queryMap)
    }

    override suspend fun loadFixturesByTeam(
        teamId: Int,
        season: Int,
        count: Int
    ): Response<FixtureDataDto> {
        val queryMap = mapOf(
            FootballApi.TEAM to teamId.toString(),
            FootballApi.SEASON to season.toString(),
            FootballApi.LAST to count.toString()
        )
        return footballApi.getFixtures(queryMap)
    }

    override suspend fun loadFixturesByTeam(teamId: Int, season: Int): Response<FixtureDataDto> {
        val queryMap = mapOf(
            FootballApi.TEAM to teamId.toString(),
            FootballApi.SEASON to season.toString()
        )
        return footballApi.getFixtures(queryMap)
    }

    override suspend fun loadFixturesLive(): Response<FixtureDataDto> {
        val queryMap = mapOf(FootballApi.LIVE to FootballApi.ALL)
        return footballApi.getFixtures(queryMap)
    }

    override suspend fun loadFixtureById(id: Int): Response<FixtureDataDto> {
        val queryMap = mapOf(FootballApi.ID to id.toString())
        return footballApi.getFixtures(queryMap)
    }
}
