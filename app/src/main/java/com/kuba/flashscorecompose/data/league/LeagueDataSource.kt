package com.kuba.flashscorecompose.data.league

import com.kuba.flashscorecompose.data.league.model.League
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/3/2022
 */
interface LeagueDataSource {
    fun observeLeagues(): Flow<List<League>>
    suspend fun getLeagues(countryNames: List<String>): List<League>
}