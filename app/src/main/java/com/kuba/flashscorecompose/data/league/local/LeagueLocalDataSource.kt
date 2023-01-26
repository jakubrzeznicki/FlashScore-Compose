package com.kuba.flashscorecompose.data.league.local

import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/1/2022
 */
interface LeagueLocalDataSource {
    fun observeLeagues(countryCode: String): Flow<List<LeagueEntity>>
    fun observeLeagues(countryCodes: List<String>): Flow<List<LeagueEntity>>
    suspend fun getLeagues(countryNames: List<String>): List<LeagueEntity>
    suspend fun getLeagueById(id: Int): LeagueEntity
    fun saveLeagues(leagues: List<LeagueEntity>)
    fun deleteLeagues(countryCode: String)
}