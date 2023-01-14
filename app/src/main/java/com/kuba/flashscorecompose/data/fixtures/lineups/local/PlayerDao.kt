package com.kuba.flashscorecompose.data.fixtures.lineups.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 12/01/2023.
 */
@Dao
interface PlayerDao {

    @Query("SELECT * FROM player WHERE id = :playerId")
    fun getPlayer(playerId: Int): Flow<PlayerEntity>

    @Query("SELECT * FROM player WHERE teamId = :teamId")
    fun getPlayersByTeamId(teamId: Int): Flow<PlayerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayers(players: List<PlayerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun deletePlayers(players: List<PlayerEntity>)
}