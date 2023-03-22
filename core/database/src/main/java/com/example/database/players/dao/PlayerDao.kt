package com.example.database.players.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.players.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 12/01/2023.
 */
@Dao
interface PlayerDao {

    @Query("SELECT * FROM player WHERE id = :playerId")
    fun observePlayer(playerId: Int): Flow<PlayerEntity?>

    @Query("SELECT * FROM player WHERE team_id = :teamId AND season = :season")
    fun observePlayers(teamId: Int, season: Int): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM player")
    fun observePlayers(): Flow<List<PlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayers(players: List<PlayerEntity>)
}
