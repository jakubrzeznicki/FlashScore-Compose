package com.example.data.team.local

import com.example.database.teams.model.CoachEntity
import com.example.database.teams.model.TeamEntity
import com.example.database.teams.model.VenueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 27/01/2023.
 */
interface TeamLocalDataSource {
    fun observeTeam(teamId: Int): Flow<TeamEntity?>
    fun observeVenue(teamId: Int): Flow<VenueEntity?>
    fun observeCoach(teamId: Int): Flow<CoachEntity?>
    fun observeTeams(): Flow<List<TeamEntity>>
    fun observeVenues(): Flow<List<VenueEntity>>
    fun observeCoaches(): Flow<List<CoachEntity>>
    suspend fun saveTeam(teamEntity: TeamEntity)
    suspend fun saveVenue(venueEntity: VenueEntity)
    suspend fun saveCoach(coachEntity: CoachEntity)
    suspend fun saveTeams(teamEntities: List<TeamEntity>)
    suspend fun saveVenues(venueEntities: List<VenueEntity>)
}
