package com.kuba.flashscorecompose.data.fixtures.fixture.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.TeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.VenueEntity

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface VenueDao {

//    @Query("SELECT * FROM venue WHERE  id= :id")
//    fun getVenue(leagueId: Int): List<TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveVenue(venues: List<VenueEntity>)

//    @Query("DELETE FROM venue WHERE venue_id = :venueId")
//    fun deleteByRound(venueId: Int)
}