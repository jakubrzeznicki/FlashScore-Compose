package com.kuba.flashscorecompose.data.standings.local

import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.
 */
interface StandingsLocalDataSource {
    fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<StandingsEntity>>
    suspend fun getStanding(leagueId: Int, season: Int): StandingsEntity
    suspend fun saveStandings(standings: List<StandingsEntity>)
}