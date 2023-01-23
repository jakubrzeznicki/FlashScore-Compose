package com.kuba.flashscorecompose.data.fixtures.fixture.local

import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.FixtureEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.TeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.VenueEntity
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface FixtureLocalDataSource {
    fun observeFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Flow<List<FixtureEntity>>

    fun observeFixturesHeadToHead(h2h: String): Flow<List<FixtureEntity>>
    fun observeFixturesByTeam(teamId: Int, season: Int): Flow<List<FixtureEntity>>
    fun observeFixturesByDate(date: String, countryNames: List<String>): Flow<List<FixtureEntity>>
    fun observeXLastFixtures(count: Int, countryNames: List<String>): Flow<List<FixtureEntity>>
    suspend fun getFixture(fixtureId: Int): FixtureEntity?
    suspend fun getFixturesByCountry(countryNames: List<String>): List<FixtureEntity>
    fun saveFixtures(fixtures: List<FixtureEntity>)
    fun saveVenues(venues: List<VenueEntity>)
    fun saveLeagues(leagues: List<LeagueEntity>)
    suspend fun saveTeams(teams: List<TeamEntity>)
    fun deleteFixturesByRound(leagueId: Int, season: Int, round: String)
    fun deleteFixturesByH2H(h2h: String)
}