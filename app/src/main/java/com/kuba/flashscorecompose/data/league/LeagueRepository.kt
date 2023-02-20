package com.kuba.flashscorecompose.data.league

import com.kuba.flashscorecompose.data.league.local.LeagueLocalDataSource
import com.kuba.flashscorecompose.data.league.mapper.toLeague
import com.kuba.flashscorecompose.data.league.model.League
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by jrzeznicki on 10/3/2022
 */
class LeagueRepository(private val local: LeagueLocalDataSource, ) : LeagueDataSource {
    override fun observeLeagues(): Flow<List<League>> =
        local.observeLeagues().map { leagueEntities -> leagueEntities.map { it.toLeague() } }

    override suspend fun getLeagues(countryNames: List<String>): List<League> =
        local.getLeagues(countryNames).map { it.toLeague() }
}