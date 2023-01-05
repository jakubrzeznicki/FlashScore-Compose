package com.kuba.flashscorecompose.data.fixtures.lineups.local

import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.LineupEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
interface LineupLocalDataSource {
    fun observeLineups(fixtureId: Int): Flow<List<LineupEntity>>
    fun saveLineups(lineups: List<LineupEntity>)
    fun deleteLineups(fixtureId: Int)
}