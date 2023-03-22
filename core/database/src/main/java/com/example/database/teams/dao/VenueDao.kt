package com.example.database.teams.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.teams.model.VenueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface VenueDao {
    @Query("SELECT * FROM venue WHERE team_id = :teamId")
    fun observeVenueByTeam(teamId: Int): Flow<VenueEntity?>

    @Query("SELECT * FROM venue")
    fun observeVenues(): Flow<List<VenueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVenues(venues: List<VenueEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVenues(venue: VenueEntity)
}
