package com.kuba.flashscorecompose.data.standings

import com.kuba.flashscorecompose.data.standings.model.Standing
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.
 */
interface StandingsDataSource {
    fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<Standing>>
    fun observeStanding(leagueId: Int, season: Int): Flow<Standing?>
    suspend fun saveStandings(standings: List<Standing>)
    suspend fun loadStandings(leagueId: Int, season: Int): RepositoryResult<List<Standing>>
}
