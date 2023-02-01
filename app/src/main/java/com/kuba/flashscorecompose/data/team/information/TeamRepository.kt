package com.kuba.flashscorecompose.data.team.information

import com.kuba.flashscorecompose.data.team.information.local.TeamLocalDataSource
import com.kuba.flashscorecompose.data.team.information.mapper.*
import com.kuba.flashscorecompose.data.team.information.model.Coach
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.TeamWithVenue
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.data.team.information.remote.TeamRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 26/01/2023.
 */
class TeamRepository(
    private val local: TeamLocalDataSource,
    private val remote: TeamRemoteDataSource
) : TeamDataSource {
    override suspend fun getTeam(teamId: Int): Team? {
        return local.getTeam(teamId)?.toTeam()
    }

    override fun observeTeam(teamId: Int): Flow<Team> {
        return local.observeTeam(teamId).map { it.toTeam() }
    }

    override fun observeVenue(teamId: Int): Flow<Venue?> {
        return local.observeVenue(teamId).map { it?.toVenue() }
    }

    override fun observeCoach(teamId: Int): Flow<Coach?> {
        return local.observeCoach(teamId).map { it?.toCoach() }
    }

    override suspend fun saveTeam(team: Team, leagueId: Int) {
        return local.saveTeam(team.toTeamEntity(leagueId))
    }

    override suspend fun saveVenue(venue: Venue) {
        return local.saveVenue(venue.toVenueEntity())
    }

    override suspend fun saveCoach(coach: Coach) {
        return local.saveCoach(coach.toCoachEntity())
    }

    override suspend fun loadTeamInformation(
        teamId: Int,
        leagueId: Int
    ): RepositoryResult<TeamWithVenue> {
        val result = remote.loadTeamInformation(teamId)
        return try {
            val teamResponseData = result.body()?.response?.firstOrNull()
            val team = teamResponseData?.team
            val venue = teamResponseData?.venue
            val teamWithVenue = TeamWithVenue(
                team = team?.toTeam() ?: Team.EMPTY_TEAM,
                venue = venue?.toVenue(teamId) ?: Venue.EMPTY_VENUE
            )
            saveTeam(team?.toTeam() ?: Team.EMPTY_TEAM, leagueId)
            saveVenue(venue?.toVenue(teamId) ?: Venue.EMPTY_VENUE)
            RepositoryResult.Success(teamWithVenue)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message()
                internalStatus = e.code()
            })
        }
    }

    override suspend fun loadCoach(teamId: Int): RepositoryResult<Coach> {
        val result = remote.loadCoach(teamId)
        return try {
            val coach = result.body()?.response?.firstOrNull()?.toCoach(teamId) ?: Coach.EMPTY_COACH
            saveCoach(coach)
            RepositoryResult.Success(coach)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message()
                internalStatus = e.code()
            })
        }
    }
}