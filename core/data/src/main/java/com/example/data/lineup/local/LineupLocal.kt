package com.example.data.lineup.local

import com.example.database.RoomStorage
import com.example.database.fixtures.lineups.model.LineupEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 04/01/2023.
 */
class LineupLocal(private val roomStorage: RoomStorage) : LineupLocalDataSource {
    override fun observeLineups(fixtureId: Int): Flow<List<LineupEntity>> {
        return roomStorage.getDatabase().lineupDao().observeLineups(fixtureId)
    }

    override suspend fun saveLineups(lineups: List<LineupEntity>) {
        roomStorage.getDatabase().lineupDao().saveLineups(lineups)
    }
}
