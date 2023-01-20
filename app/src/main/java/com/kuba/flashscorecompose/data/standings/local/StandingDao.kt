package com.kuba.flashscorecompose.data.standings.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.
 */
@Dao
interface StandingDao {

    @Query("SELECT * FROM standings WHERE primary_league_id IN(:leagueIds) AND season = :season")
    fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<StandingsEntity>>

    @Query("SELECT * FROM standings WHERE primary_league_id = :leagueId AND season = :season")
    suspend fun getStanding(leagueId: Int, season: Int): StandingsEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStandings(standings: List<StandingsEntity>)
}