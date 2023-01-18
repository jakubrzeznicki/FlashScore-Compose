package com.kuba.flashscorecompose.data.standings.local

import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.
 */
interface StandingsLocalDataSource {
    fun observeStandings(leagueId: Int, season: Int): Flow<List<StandingsEntity>>
    suspend fun saveStandings(standings: List<StandingsEntity>)
}