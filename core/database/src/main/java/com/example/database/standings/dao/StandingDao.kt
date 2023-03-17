package com.example.database.standings.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.standings.model.StandingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.
 */
@Dao
interface StandingDao {

    @Query("SELECT * FROM standings WHERE primary_league_id IN(:leagueIds) AND season = :season")
    fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<StandingsEntity>>

    @Query("SELECT * FROM standings WHERE primary_league_id = :leagueId AND season = :season LIMIT 1")
    fun observeStanding(leagueId: Int, season: Int): Flow<StandingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStandings(standings: List<StandingsEntity>)
}