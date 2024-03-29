package com.example.database.fixtures.matches.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.fixtures.matches.model.FixtureEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface FixtureDao {

    @Query("SELECT * FROM fixture WHERE league_id = :leagueId AND league_season = :season AND league_round = :round")
    fun observeFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE h2h = :h2h")
    fun observeFixturesHeadToHead(h2h: String): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE home_team_id = :teamId OR away_team_id = :teamId AND league_season = :season")
    fun observeFixturesByTeam(teamId: Int, season: Int): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE fixture_info_short_date = :date AND league_countryName IN(:countryNames) ORDER BY fixture_info_timestamp")
    fun observeFixturesByDate(date: String, countryNames: List<String>): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE league_id = :leagueId")
    fun observeFixturesByLeague(leagueId: Int): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE league_countryName IN(:countryNames) ORDER BY fixture_info_timestamp LIMIT :count")
    fun observeXLastFixtures(count: Int, countryNames: List<String>): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE fixture_info_is_live = 1")
    fun observeFixturesLive(): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE id IN(:ids)")
    fun observeFavoriteFixtures(ids: List<Int>): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixture WHERE fixture_info_id = :id")
    fun observeFixtureById(id: Int): Flow<FixtureEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFixtures(fixtures: List<FixtureEntity>)
}
