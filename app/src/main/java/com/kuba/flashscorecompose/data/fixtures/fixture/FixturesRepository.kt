package com.kuba.flashscorecompose.data.fixtures.fixture

import com.kuba.flashscorecompose.data.fixtures.fixture.local.FixtureLocalDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toFixtureEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toFixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.FixtureRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        return try {
            val fixtureItems =
                result.body()?.response?.map { it.toFixtureItem(leagueId, season, round) }
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }

    override suspend fun loadFixturesHeadToHead(h2h: String): RepositoryResult<List<FixtureItem>> {
        val result = remote.loadFixturesHeadToHead(h2h = h2h)
        return try {
            val fixtureItems = result.body()?.response?.map { it.toFixtureItem(h2h) }
            RepositoryResult.Success(fixtureItems)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}