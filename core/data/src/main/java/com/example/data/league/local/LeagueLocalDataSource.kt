package com.example.data.league.local

import com.example.database.leagues.model.LeagueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/1/2022
 */
interface LeagueLocalDataSource {
    fun observeLeagues(): Flow<List<LeagueEntity>>
    suspend fun getLeagues(countryNames: List<String>): List<LeagueEntity>
}
