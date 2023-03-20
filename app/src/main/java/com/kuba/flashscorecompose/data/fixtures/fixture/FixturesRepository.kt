package com.kuba.flashscorecompose.data.fixtures.fixture

import com.kuba.flashscorecompose.data.fixtures.fixture.local.FixtureLocalDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toFixtureEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toFixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.FixtureRemoteDataSource
import com.kuba.flashscorecompose.data.league.mapper.toLeagueEntity
import com.kuba.flashscorecompose.data.team.information.mapper.toTeamEntity
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

    override fun observeFixtureByLeague(leagueId: Int): Flow<List<FixtureItem>> {
        return local.observeFixturesByLeague(leagueId).map { fixtureEntities ->
            fixtureEntities.map { it.toFixtureItem() }
        }
    }

    override fun observeFixturesLive(): Flow<List<FixtureItem>> {
        return local.observeFixturesLive().map { fixtureEntities ->
            fixtureEntities.map { it.toFixtureItem() }
        }
    }

    override fun observeFavoriteFixtures(ids: List<Int>): Flow<List<FixtureItem>> {
        return local.observeFavoriteFixtures(ids).map { fixtureEntities ->
            fixtureEntities.map { it.toFixtureItem() }
        }
    }

    override fun observeFixtureById(id: Int): Flow<FixtureItem?> {
        return local.observeFixtureById(id).map { fixtureEntity ->
            fixtureEntity?.toFixtureItem()
        }
    }

    override suspend fun saveFixtureItem(fixtureItems: List<FixtureItem>) {
        local.saveFixtures(fixtureItems.map { it.toFixtureEntity() })
    }

    override suspend fun loadFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): RepositoryResult<List<FixtureItem>> {
        val result =
            remote.loadFixturesFilteredByRound(leagueId = leagueId, season = season, round = round)
        return try {
            val fixtureItems = result.body()?.response?.map {
                it.toFixtureItem(season = season, round = round)
            }
            saveData(fixtureItems)
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }

    override suspend fun loadFixturesHeadToHead(
        h2h: String,
        count: Int
    ): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesHeadToHead(h2h = h2h, count = count)
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItem(h2h = h2h) }
            saveData(fixtureItems)
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }

    override suspend fun loadFixturesByDate(date: String): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesByDate(date)
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItem() }
            saveData(fixtureItems)
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }

    override suspend fun loadFixturesByDate(
        date: String,
        leagueId: Int,
        season: Int
    ): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesByDate(date)
        return try {
            val fixtureItems =
                result.body()?.response?.map { it.toFixtureItem(season = season) }
            saveData(fixtureItems)
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }

    override suspend fun loadFixturesByTeam(
        teamId: Int,
        season: Int,
        count: Int
    ): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesByTeam(teamId, season, count)
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItem(season = season) }
            saveData(fixtureItems)
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }

    override suspend fun loadFixturesByTeam(
        teamId: Int,
        season: Int
    ): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesByTeam(teamId, season)
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItem(season = season) }
            saveData(fixtureItems)
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }

    override suspend fun loadFixtureById(id: Int): RepositoryResult<FixtureItem> {
        val result = remote.loadFixtureById(id)
        return try {
            val fixtureItem = result.body()?.response?.map { it.toFixtureItem() }
            saveData(fixtureItem)
            RepositoryResult.Success(fixtureItem?.first())
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }

    override suspend fun loadFixturesLive(): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesLive()
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItem() }
            saveData(fixtureItems)
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(
                ResponseStatus().apply {
                    this.statusMessage = e.message()
                    this.internalStatus = e.code()
                }
            )
        }
    }

    private suspend fun saveData(fixtureItems: List<FixtureItem>?) {
        withContext(Dispatchers.IO) {
            saveFixtureItem(fixtureItems.orEmpty())
            local.saveLeagues(fixtureItems?.map { it.league.toLeagueEntity() }.orEmpty())
            local.saveTeams(
                fixtureItems?.map {
                    it.homeTeam.toTeamEntity(it.league.id, it.league.season)
                }.orEmpty()
            )
            local.saveTeams(
                fixtureItems?.map {
                    it.awayTeam.toTeamEntity(it.league.id, it.league.season)
                }.orEmpty()
            )
        }
    }
}
