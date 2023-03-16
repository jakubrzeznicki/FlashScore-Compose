package com.example.data.standings.repository

import com.example.common.utils.RepositoryResult
import com.example.model.standings.Standing
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
