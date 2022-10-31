package com.kuba.flashscorecompose.data.league.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.league.model.League
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/1/2022
 */
@Dao
interface LeagueDao {
    @Query("SELECT * FROM leagues WHERE countryId = :countryId")
    fun observeLeagues(countryId: String): Flow<List<League>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveLeagues(leagues: List<League>)

    @Query("DELETE from leagues WHERE countryId = :countryId")
    fun deleteLeagues(countryId: String)
}