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
    fun observeFixturesByDate(date: String): Flow<List<FixtureItem>>
    fun observeXLastFixtures(count: Int, countryNames: List<String>): Flow<List<FixtureItem>>
    fun saveFixtureItem(fixtureItems: List<FixtureItem>)
    suspend fun loadFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): RepositoryResult<List<FixtureItem>>

    suspend fun loadFixturesHeadToHead(h2h: String): RepositoryResult<List<FixtureItem>>
    suspend fun loadFixturesByDate(date: String): RepositoryResult<List<FixtureItem>>
    suspend fun loadLastXFixtures(count: Int): RepositoryResult<List<FixtureItem>>
}