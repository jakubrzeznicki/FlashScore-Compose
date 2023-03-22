package com.example.data.lineup.local

import com.example.database.fixtures.lineups.model.LineupEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
interface LineupLocalDataSource {
    fun observeLineups(fixtureId: Int): Flow<List<LineupEntity>>
    suspend fun saveLineups(lineups: List<LineupEntity>)
}
