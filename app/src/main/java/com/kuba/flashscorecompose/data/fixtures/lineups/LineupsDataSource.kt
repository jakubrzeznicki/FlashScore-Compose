package com.kuba.flashscorecompose.data.fixtures.lineups

import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.utils.RepositoryResult
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
