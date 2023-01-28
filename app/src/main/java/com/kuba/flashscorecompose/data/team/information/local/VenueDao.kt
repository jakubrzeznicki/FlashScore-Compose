package com.kuba.flashscorecompose.data.team.information.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.team.information.local.model.VenueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
@Dao
interface VenueDao {
    @Query("SELECT * FROM venue WHERE team_id = :teamId")
    fun observeVenueByTeam(teamId: Int): Flow<VenueEntity?>

    @Query("SELECT * FROM venue WHERE team_id = :teamId")
    suspend fun getVenueByTeam(teamId: Int): VenueEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVenue(venues: List<VenueEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVenue(venue: VenueEntity)
}