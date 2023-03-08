package com.kuba.flashscorecompose.api

import com.kuba.flashscorecompose.data.country.remote.model.CountryDataDto
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.FixtureDataDto
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.LineupDataDto
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticsDataDto
import com.kuba.flashscorecompose.data.players.remote.model.PlayersDataDto
import com.kuba.flashscorecompose.data.standings.remote.model.StandingsDataDto
import com.kuba.flashscorecompose.data.team.information.remote.model.CoachDataDto
import com.kuba.flashscorecompose.data.team.information.remote.model.TeamDataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by jrzeznicki on 9/5/2022
 */
interface FootballApi {

    @GET("$API_VERSION/$COUNTRIES")
    suspend fun getCountries(): Response<CountryDataDto>

    @GET("$API_VERSION/$FIXTURES")
    suspend fun getFixtures(@QueryMap queryMap: Map<String, String>): Response<FixtureDataDto>

    @GET("$API_VERSION/$FIXTURES/$HEAD_TO_HEAD")
    suspend fun getFixturesHeadToHead(
        @Query(H2H) h2h: String,
        @Query(LAST) count: Int
    ): Response<FixtureDataDto>

    @GET("$API_VERSION/$FIXTURES/$STATISTICS")
    suspend fun getFixturesStatistics(@Query(FIXTURE) fixtureId: Int): Response<StatisticsDataDto>

    @GET("$API_VERSION/$FIXTURES/$LINEUPS")
    suspend fun getFixturesLineups(@Query(FIXTURE) fixtureId: Int): Response<LineupDataDto>

    @GET("$API_VERSION/$STANDINGS")
    suspend fun getStandings(
        @Query(SEASON) season: Int,
        @Query(LEAGUE) leagueId: Int
    ): Response<StandingsDataDto>

    @GET("$API_VERSION/$TEAMS")
    suspend fun getTeams(@QueryMap queryMap: Map<String, String>): Response<TeamDataDto>

    @GET("$API_VERSION/$COACHS")
    suspend fun getCoachByTeam(@Query(TEAM) teamId: Int): Response<CoachDataDto>

    @GET("$API_VERSION/$PLAYERS")
    suspend fun getPlayers(@QueryMap queryMap: Map<String, String>): Response<PlayersDataDto>

    companion object {
        const val API_VERSION = "v3"
        const val COUNTRIES = "countries"
        const val COUNTRY = "country"
        const val LEAGUE = "league"
        const val SEASON = "season"
        const val FIXTURES = "fixtures"
        const val ROUND = "round"
        const val FIXTURE = "fixture"
        const val STATISTICS = "statistics"
        const val LINEUPS = "lineups"
        const val HEAD_TO_HEAD = "headtohead"
        const val H2H = "h2h"
        const val DATE = "date"
        const val LAST = "last"
        const val TEAM = "team"
        const val TEAMS = "teams"
        const val ID = "id"
        const val STANDINGS = "standings"
        const val COACHS = "coachs"
        const val PLAYERS = "players"
        const val LIVE = "live"
        const val ALL = "all"
    }
}
