package com.kuba.flashscorecompose.data.league.local

import com.kuba.flashscorecompose.data.league.model.League
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/1/2022
 */
interface LeagueLocalDataSource {
    fun observeLeagues(countryId: String): Flow<List<League>>
    fun saveLeagues(leagues: List<League>)
    fun deleteLeagues(countryId: String)
}