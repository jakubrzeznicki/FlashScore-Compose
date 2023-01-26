package com.kuba.flashscorecompose.data.fixtures.fixture

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface FixturesDataSource {
    fun observeFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Flow<List<FixtureItem>>

    fun observeFixturesHeadToHead(h2h: String): Flow<List<FixtureItem>>
    fun observeFixturesByTeam(
        teamId: Int,
        season: Int
    ): Flow<List<FixtureItem>>

    fun observeFixturesByDate(date: String, countryNames: List<String>): Flow<List<FixtureItem>>
    fun observeFixtureByLeague(leagueId: Int): Flow<List<FixtureItem>>
    fun observeXLastFixtures(count: Int, countryNames: List<String>): Flow<List<FixtureItem>>
    suspend fun getFixture(fixtureId: Int): FixtureItem?
    suspend fun getFixturesByCountry(countryNames: List<String>): List<FixtureItem>
    fun saveFixtureItem(fixtureItems: List<FixtureItem>)
    suspend fun loadFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): RepositoryResult<List<FixtureItem>>

    suspend fun loadFixturesHeadToHead(h2h: String, count: Int): RepositoryResult<List<FixtureItem>>
    suspend fun loadFixturesByDate(queryMap: Map<String, String>): RepositoryResult<List<FixtureItem>>
    suspend fun loadLastXFixtures(count: Int): RepositoryResult<List<FixtureItem>>
    suspend fun loadFixturesByTeam(
        teamId: Int,
        season: Int,
        count: Int
    ): RepositoryResult<List<FixtureItem>>

}