package com.kuba.flashscorecompose.data.standings.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 18/01/2023.
 */
interface StandingDao {

    @Query("SELECT * FROM standings WHERE primary_league_id = :leagueId AND season = season")
    fun observeStandings(leagueId: Int, season: Int): Flow<List<StandingsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStandings(standings: List<StandingsEntity>)
}