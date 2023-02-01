package com.kuba.flashscorecompose.data.league

import com.kuba.flashscorecompose.data.league.local.LeagueLocalDataSource
import com.kuba.flashscorecompose.data.league.mapper.toLeague
import com.kuba.flashscorecompose.data.league.mapper.toLeagueEntity
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.league.remote.LeagueRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 10/3/2022
 */
class LeagueRepository(
    private val local: LeagueLocalDataSource,
    private val remote: LeagueRemoteDataSource
) : LeagueDataSource {

    override fun observeLeagues(countryCode: String): Flow<List<League>> =
        local.observeLeagues(countryCode)
            .map { leagueEntity -> leagueEntity.map { it.toLeague() } }

    override fun observeLeagues(countryCodes: List<String>): Flow<List<League>> =
        local.observeLeagues(countryCodes)
            .map { leagueEntity -> leagueEntity.map { it.toLeague() } }

    override fun observeLeague(id: Int): Flow<League?> =
        local.observeLeague(id).map { it?.toLeague() }

    override suspend fun getLeagues(countryNames: List<String>): List<League> =
        local.getLeagues(countryNames).map { it.toLeague() }

    override fun saveLeagues(leagues: List<League>) {
        local.saveLeagues(leagues.map { it.toLeagueEntity() })
    }

    override suspend fun loadLeagues(countryCode: String): RepositoryResult<List<League>> {
        val result = remote.loadLeagues(countryCode)
        return try {
            val leagues = result.body()?.response?.mapNotNull { it.league?.toLeague() }.orEmpty()
            withContext(Dispatchers.IO) {
                saveLeagues(leagues)
            }
            RepositoryResult.Success(leagues)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }

    override suspend fun loadLeague(id: Int): RepositoryResult<League> {
        val result = remote.loadLeague(id)
        return try {
            val league =
                result.body()?.response?.mapNotNull { it.league?.toLeague() }.orEmpty().first()
            withContext(Dispatchers.IO) {
                saveLeagues(listOf(league))
            }
            RepositoryResult.Success(league)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}