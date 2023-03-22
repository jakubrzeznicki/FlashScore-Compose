package com.example.database.players.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 12/01/2023.
 */
@Dao
interface PlayerDao {

    @Query("SELECT * FROM player WHERE id = :playerId")
    fun observePlayer(playerId: Int): Flow<com.example.database.players.model.PlayerEntity?>

    @Query("SELECT * FROM player WHERE team_id = :teamId AND season = :season")
    fun observePlayers(teamId: Int, season: Int): Flow<List<com.example.database.players.model.PlayerEntity>>

    @Query("SELECT * FROM player")
    fun observePlayers(): Flow<List<com.example.database.players.model.PlayerEntity>>

    @Query("SELECT * FROM player WHERE id IN(:ids)")
    suspend fun getFavoritePlayers(ids: List<Int>): List<com.example.database.players.model.PlayerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayers(players: List<com.example.database.players.model.PlayerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun deletePlayers(players: List<com.example.database.players.model.PlayerEntity>)
}
