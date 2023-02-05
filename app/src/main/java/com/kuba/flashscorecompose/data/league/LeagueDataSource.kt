package com.kuba.flashscorecompose.data.league

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/3/2022
 */
interface LeagueDataSource {
    fun observeLeagues(): Flow<List<League>>
    fun observeLeagues(countryCode: String): Flow<List<League>>
    fun observeLeagues(countryCodes: List<String>): Flow<List<League>>
    fun observeLeague(id: Int): Flow<League?>
    suspend fun getLeagues(countryNames: List<String>): List<League>
    fun saveLeagues(leagues: List<League>)
    suspend fun loadLeagues(countryCode: String): RepositoryResult<List<League>>
    suspend fun loadLeague(id: Int): RepositoryResult<League>
}