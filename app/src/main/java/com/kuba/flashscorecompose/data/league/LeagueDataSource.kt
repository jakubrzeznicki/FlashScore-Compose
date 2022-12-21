package com.kuba.flashscorecompose.data.league

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/3/2022
 */
interface LeagueDataSource {
    fun observeLeagues(countryCode: String): Flow<List<League>>
    fun saveLeagues(leagues: List<League>)
    suspend fun loadLeagues(countryCode: String): RepositoryResult<List<League>>
}