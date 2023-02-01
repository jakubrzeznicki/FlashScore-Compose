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
    override suspend fun getTeam(teamId: Int): TeamEntity? {
        return roomStorage.getDatabase().teamDao().getTeam(teamId)
    }

    override fun observeTeam(teamId: Int): Flow<TeamEntity> {
        return roomStorage.getDatabase().teamDao().observeTeam(teamId)
    }

    override fun observeVenue(teamId: Int): Flow<VenueEntity?> {
        return roomStorage.getDatabase().venueDao().observeVenueByTeam(teamId)
    }

    override fun observeCoach(teamId: Int): Flow<CoachEntity?> {
        return roomStorage.getDatabase().coachDao().observeCoachByTeam(teamId)
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
}