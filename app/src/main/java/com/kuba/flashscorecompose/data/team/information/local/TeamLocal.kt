package com.kuba.flashscorecompose.data.team.information.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.team.information.local.model.CoachEntity
import com.kuba.flashscorecompose.data.team.information.local.model.TeamEntity
import com.kuba.flashscorecompose.data.team.information.local.model.VenueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 27/01/2023.
 */
class TeamLocal(private val roomStorage: RoomStorage) : TeamLocalDataSource {

    override fun observeTeam(teamId: Int): Flow<TeamEntity?> {
        return roomStorage.getDatabase().teamDao().observeTeam(teamId)
    }

    override fun observeVenue(teamId: Int): Flow<VenueEntity?> {
        return roomStorage.getDatabase().venueDao().observeVenueByTeam(teamId)
    }

    override fun observeCoach(teamId: Int): Flow<CoachEntity?> {
        return roomStorage.getDatabase().coachDao().observeCoachByTeam(teamId)
    }

    override fun observeTeams(): Flow<List<TeamEntity>> {
        return roomStorage.getDatabase().teamDao().observeTeams()
    }

    override fun observeVenues(): Flow<List<VenueEntity>> {
        return roomStorage.getDatabase().venueDao().observeVenues()
    }

    override fun observeCoaches(): Flow<List<CoachEntity>> {
        return roomStorage.getDatabase().coachDao().observeCoaches()
    }

    override suspend fun saveTeam(teamEntity: TeamEntity) {
        roomStorage.getDatabase().teamDao().saveTeam(teamEntity)
    }

    override suspend fun saveVenue(venueEntity: VenueEntity) {
        roomStorage.getDatabase().venueDao().saveVenue(venueEntity)
    }

    override suspend fun saveCoach(coachEntity: CoachEntity) {
        roomStorage.getDatabase().coachDao().upsertCoach(coachEntity)
    }

    override suspend fun saveTeams(teamEntities: List<TeamEntity>) {
        roomStorage.getDatabase().teamDao().saveTeams(teamEntities)
    }

    override suspend fun saveVenues(venueEntities: List<VenueEntity>) {
        roomStorage.getDatabase().venueDao().saveVenue(venueEntities)
    }
}
