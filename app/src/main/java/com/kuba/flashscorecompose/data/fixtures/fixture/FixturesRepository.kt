package com.kuba.flashscorecompose.data.fixtures.fixture

import android.util.Log
import com.kuba.flashscorecompose.data.fixtures.fixture.local.FixtureLocalDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.*
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.FixtureRemoteDataSource
import com.kuba.flashscorecompose.data.league.mapper.toLeagueEntity
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class FixturesRepository(
    private val local: FixtureLocalDataSource,
    private val remote: FixtureRemoteDataSource
) : FixturesDataSource {

    override fun observeFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Flow<List<FixtureItem>> {
        return local.observeFixturesFilteredByRound(leagueId, season, round)
            .map { fixtureEntities ->
                fixtureEntities.map { it.toFixtureItem() }
            }
    }

    override fun observeFixturesHeadToHead(h2h: String): Flow<List<FixtureItem>> {
        return local.observeFixturesHeadToHead(h2h).map { fixtureEntities ->
            fixtureEntities.map { it.toFixtureItem() }
        }
    }

    override fun observeFixturesByTeam(teamId: Int, season: Int): Flow<List<FixtureItem>> {
        return local.observeFixturesByTeam(teamId, season).map { fixtureEntities ->
            fixtureEntities.map { it.toFixtureItem() }
        }
    }

    override fun observeFixturesByDate(
        date: String,
        countryNames: List<String>
    ): Flow<List<FixtureItem>> {
        return local.observeFixturesByDate(date, countryNames).map { fixtureEntities ->
            fixtureEntities.map { it.toFixtureItem() }
        }
    }

    override fun observeXLastFixtures(
        count: Int,
        countryNames: List<String>
    ): Flow<List<FixtureItem>> {
        return local.observeXLastFixtures(count, countryNames).map { fixtureEntities ->
            fixtureEntities.map { it.toFixtureItem() }
        }
    }

    override suspend fun getFixture(fixtureId: Int): FixtureItem {
        return local.getFixture(fixtureId).toFixtureItem()
    }

    override fun saveFixtureItem(fixtureItems: List<FixtureItem>) {
        local.saveFixtures(fixtureItems.map { it.toFixtureEntity() })
    }

    override suspend fun loadFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): RepositoryResult<List<FixtureItem>> {
        val result =
            remote.loadFixturesFilteredByRound(leagueId = leagueId, season = season, round = round)
        Log.d("TEST_LOG", "Result message = ${result.message()}")
        Log.d("TEST_LOG", "Result code = ${result.code()}")
        Log.d("TEST_LOG", "Result body T= ${result.body()?.response}")
        return try {
            val fixtureItems =
                result.body()?.response?.map { it.toFixtureItem(leagueId, season, round) }
            withContext(Dispatchers.IO) {
                local.saveFixtures(fixtureItems?.map { it.toFixtureEntity() }.orEmpty())
                local.saveLeagues(fixtureItems?.map { it.league.toLeagueEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.homeTeam.toTeamEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.awayTeam.toTeamEntity() }.orEmpty())
                // local.saveVenues(fixtureItems?.map { it.fixture.venue?.toVenueEntity() }.orEmpty())
            }
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }

    override suspend fun loadFixturesHeadToHead(
        h2h: String,
        count: Int
    ): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesHeadToHead(h2h = h2h, count = count)
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItem(h2h) }
            withContext(Dispatchers.IO) {
                local.saveFixtures(fixtureItems?.map { it.toFixtureEntity() }.orEmpty())
                local.saveLeagues(fixtureItems?.map { it.league.toLeagueEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.homeTeam.toTeamEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.awayTeam.toTeamEntity() }.orEmpty())
                // local.saveVenues(fixtureItems?.map { it.fixture.venue?.toVenueEntity() }.orEmpty())
            }
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }

    override suspend fun loadFixturesByDate(date: String): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesByDate(date = date)
        Log.d("TEST_LOG", "Result message = ${result.message()}")
        Log.d("TEST_LOG", "Result code = ${result.code()}")
        Log.d("TEST_LOG", "Result body T= ${result.body()?.response}")
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItemWithDate(date) }
            withContext(Dispatchers.IO) {
                local.saveFixtures(fixtureItems?.map { it.toFixtureEntity() }.orEmpty())
                local.saveLeagues(fixtureItems?.map { it.league.toLeagueEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.homeTeam.toTeamEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.awayTeam.toTeamEntity() }.orEmpty())
                // local.saveVenues(fixtureItems?.map { it.fixture.venue?.toVenueEntity() }.orEmpty())
            }
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }

    override suspend fun loadLastXFixtures(count: Int): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadLastXFixtures(count = count)
        Log.d("TEST_LOG", "Result message = ${result.message()}")
        Log.d("TEST_LOG", "Result code = ${result.code()}")
        Log.d("TEST_LOG", "Result body T= ${result.body()?.response}")
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItemWithDate("") }
            withContext(Dispatchers.IO) {
                local.saveFixtures(fixtureItems?.map { it.toFixtureEntity() }.orEmpty())
                local.saveLeagues(fixtureItems?.map { it.league.toLeagueEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.homeTeam.toTeamEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.awayTeam.toTeamEntity() }.orEmpty())
                // local.saveVenues(fixtureItems?.map { it.fixture.venue?.toVenueEntity() }.orEmpty())
            }
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }

    override suspend fun loadFixturesByTeam(
        teamId: Int,
        season: Int,
        count: Int
    ): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesByTeam(teamId, season, count)
        Log.d("TEST_LOG", "Result message = ${result.message()}")
        Log.d("TEST_LOG", "Result code = ${result.code()}")
        Log.d("TEST_LOG", "Result body T= ${result.body()?.response}")
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItem(season) }
            withContext(Dispatchers.IO) {
                local.saveFixtures(fixtureItems?.map { it.toFixtureEntity() }.orEmpty())
                local.saveLeagues(fixtureItems?.map { it.league.toLeagueEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.homeTeam.toTeamEntity() }.orEmpty())
                local.saveTeams(fixtureItems?.map { it.awayTeam.toTeamEntity() }.orEmpty())
                // local.saveVenues(fixtureItems?.map { it.fixture.venue?.toVenueEntity() }.orEmpty())
            }
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}