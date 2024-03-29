package com.example.data.fixture.repository

import com.example.common.utils.RepositoryResult
import com.example.model.fixture.FixtureItem
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
    fun observeFixturesLive(): Flow<List<FixtureItem>>
    fun observeFavoriteFixtures(ids: List<Int>): Flow<List<FixtureItem>>
    fun observeFixtureById(id: Int): Flow<FixtureItem?>
    suspend fun saveFixtureItem(fixtureItems: List<FixtureItem>)
    suspend fun loadFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): RepositoryResult<List<FixtureItem>>

    suspend fun loadFixturesHeadToHead(h2h: String, count: Int): RepositoryResult<List<FixtureItem>>
    suspend fun loadFixturesByDate(date: String): RepositoryResult<List<FixtureItem>>
    suspend fun loadFixturesByDate(
        date: String,
        leagueId: Int,
        season: Int
    ): RepositoryResult<List<FixtureItem>>

    suspend fun loadFixturesByTeam(
        teamId: Int,
        season: Int,
        count: Int
    ): RepositoryResult<List<FixtureItem>>

    suspend fun loadFixtureById(id: Int): RepositoryResult<FixtureItem>
    suspend fun loadFixturesByTeam(teamId: Int, season: Int): RepositoryResult<List<FixtureItem>>
    suspend fun loadFixturesLive(): RepositoryResult<List<FixtureItem>>
}
