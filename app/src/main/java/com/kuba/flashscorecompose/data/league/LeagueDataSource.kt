package com.kuba.flashscorecompose.data.league

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/3/2022
 */
interface LeagueDataSource {
    fun observeLeagues(countryId: String): Flow<List<League>>
    fun saveLeagues(leagues: List<League>)
    suspend fun loadLeagues(countryId: String): RepositoryResult<List<League>>
}