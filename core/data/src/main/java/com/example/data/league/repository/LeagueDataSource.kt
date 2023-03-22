package com.example.data.league.repository

import com.example.model.league.League
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/3/2022
 */
interface LeagueDataSource {
    fun observeLeagues(): Flow<List<League>>
    suspend fun getLeagues(countryNames: List<String>): List<League>
}
