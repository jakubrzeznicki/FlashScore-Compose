package com.kuba.flashscorecompose.data.league

import com.kuba.flashscorecompose.data.league.local.LeagueLocalDataSource
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.league.remote.LeagueRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Created by jrzeznicki on 10/3/2022
 */
class LeagueRepository(
    private val local: LeagueLocalDataSource,
    private val remote: LeagueRemoteDataSource
) : LeagueDataSource {
    override fun observeLeagues(countryId: String): Flow<List<League>> =
        local.observeLeagues(countryId)

    override fun saveLeagues(leagues: List<League>) {
        local.saveLeagues(leagues)
    }

    override suspend fun loadLeagues(countryId: String): RepositoryResult<List<League>> {
        return remote.loadLeagues(countryId).also {
            withContext(Dispatchers.IO) {
                if (it is RepositoryResult.Success) {
                    local.deleteLeagues(countryId)
                    local.saveLeagues(it.data)
                }
            }
        }
    }
}