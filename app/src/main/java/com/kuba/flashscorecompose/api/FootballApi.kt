package com.kuba.flashscorecompose.api

import com.kuba.flashscorecompose.data.country.remote.model.CountryDataDto
import com.kuba.flashscorecompose.data.fixtures.currentround.remote.model.CurrentRoundDataDto
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.FixtureDataDto
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.LineupDataDto
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticsDataDto
import com.kuba.flashscorecompose.data.league.remote.model.LeagueDataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jrzeznicki on 9/5/2022
 */
interface FootballApi {

    @GET("$API_VERSION/$COUNTRIES")
    suspend fun getCountries(): Response<CountryDataDto>

    @GET("$API_VERSION/$LEAGUES")
    suspend fun getLeagues(@Query(CODE) countryCode: String): Response<LeagueDataDto>

    @GET("$API_VERSION/$FIXTURES/$ROUNDS")
    suspend fun getFixturesCurrentRound(
        @Query(LEAGUE) leagueId: Int, @Query(SEASON) season: Int, @Query(CURRENT) isCurrent: Boolean
    ): Response<CurrentRoundDataDto>

    @GET("$API_VERSION/$FIXTURES")
    suspend fun getFixturesFilteredByRound(
        @Query(LEAGUE) leagueId: Int, @Query(SEASON) season: Int, @Query(ROUND) round: String
    ): Response<FixtureDataDto>

    @GET("$API_VERSION/$FIXTURES")
    suspend fun getFixturesByDate(@Query(DATE) date: String): Response<FixtureDataDto>

    @GET("$API_VERSION/$FIXTURES/$HEAD_TO_HEAD")
    suspend fun getFixturesHeadToHead(@Query(H2H) h2h: String): Response<FixtureDataDto>

    @GET("$API_VERSION/$FIXTURES")
    suspend fun getLastXFixtures(@Query(LAST) count: Int): Response<FixtureDataDto>

    @GET("$API_VERSION/$FIXTURES/$STATISTICS")
    suspend fun getFixturesStatistics(@Query(FIXTURE) fixtureId: Int): Response<StatisticsDataDto>

    @GET("$API_VERSION/$FIXTURES/$LINEUPS")
    suspend fun getFixturesLineups(@Query(FIXTURE) fixtureId: Int): Response<LineupDataDto>

    companion object {
        const val API_VERSION = "v3"
        const val COUNTRIES = "countries"
        const val LEAGUES = "leagues"
        const val CODE = "code"
        const val CURRENT = "current"
        const val LEAGUE = "league"
        const val SEASON = "season"
        const val FIXTURES = "fixtures"
        const val ROUNDS = "rounds"
        const val ROUND = "round"
        const val FIXTURE = "fixture"
        const val STATISTICS = "statistics"
        const val LINEUPS = "lineups"
        const val HEAD_TO_HEAD = "headtohead"
        const val H2H = "h2h"
        const val DATE = "date"
        const val LAST = "last"
    }
}