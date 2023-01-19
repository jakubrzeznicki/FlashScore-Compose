package com.kuba.flashscorecompose.data.league.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/1/2022
 */
@Dao
interface LeagueDao {
    @Query("SELECT * FROM leagues WHERE countryCode = :countryCode")
    fun observeLeagues(countryCode: String): Flow<List<LeagueEntity>>

    @Query("SELECT * FROM leagues WHERE countryCode IN(:countryCodes)")
    fun observeLeagues(countryCodes: List<String>): Flow<List<LeagueEntity>>

    @Query("SELECT * FROM leagues WHERE countryName IN(:countryNames)")
    suspend fun getLeagues(countryNames: List<String>): List<LeagueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLeagues(leagues: List<LeagueEntity>)

    @Query("DELETE from leagues WHERE countryCode = :countryCode")
    fun deleteLeagues(countryCode: String)
}