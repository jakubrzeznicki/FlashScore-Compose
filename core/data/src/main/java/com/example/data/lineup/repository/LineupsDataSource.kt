package com.example.data.lineup.repository

import com.example.common.utils.RepositoryResult
import com.example.model.lineup.Lineup
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface LineupsDataSource {
    fun observeLineups(fixtureId: Int): Flow<List<Lineup>>
    suspend fun saveLineups(lineups: List<Lineup>)
    suspend fun loadLineups(
        fixtureId: Int,
        leagueId: Int,
        season: Int
    ): RepositoryResult<List<Lineup>>
}
