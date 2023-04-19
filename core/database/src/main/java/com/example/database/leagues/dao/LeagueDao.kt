package com.example.database.leagues.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.leagues.model.LeagueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/1/2022
 */
@Dao
interface LeagueDao {
    @Query("SELECT * FROM leagues WHERE id != 0")
    fun observeLeagues(): Flow<List<LeagueEntity>>

    @Query("SELECT * FROM leagues WHERE countryName IN(:countryNames)")
    suspend fun getLeagues(countryNames: List<String>): List<LeagueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLeagues(leagues: List<LeagueEntity>)
}
