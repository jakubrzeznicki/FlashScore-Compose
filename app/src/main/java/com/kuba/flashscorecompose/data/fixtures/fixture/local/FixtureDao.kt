package com.kuba.flashscorecompose.data.fixtures.fixture.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.FixtureEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface FixtureDao {

    @Query("SELECT * FROM fixture WHERE current_round_league_id = :leagueId AND current_round_season = :season AND current_round_round = :round")
    fun observeFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE h2h = :h2h")
    fun observeFixturesHeadToHead(h2h: String): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE date = :date")
    fun observeFixturesByDate(date: String): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE league_countryName IN(:countryNames) ORDER BY fixture_info_timestamp LIMIT :count")
    fun observeXLastFixtures(count: Int, countryNames: List<String>): Flow<List<FixtureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFixtures(fixtures: List<FixtureEntity>)

    @Query("DELETE FROM fixture WHERE league_id = :leagueId AND current_round_season = :season AND current_round_round = :round")
    fun deleteByRound(leagueId: Int, season: Int, round: String)

    @Query("DELETE FROM fixture WHERE h2h = :h2h")
    fun deleteByH2H(h2h: String)
}