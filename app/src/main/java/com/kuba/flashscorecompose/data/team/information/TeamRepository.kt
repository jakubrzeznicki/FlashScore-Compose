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

    override fun observeTeam(teamId: Int): Flow<Team?> {
        return local.observeTeam(teamId).map { it?.toTeam() }
    }

    override fun observeVenue(teamId: Int): Flow<Venue?> {
        return local.observeVenue(teamId).map { it?.toVenue() }
    }

    override fun observeCoach(teamId: Int): Flow<Coach?> {
        return local.observeCoach(teamId).map { it?.toCoach() }
    }

    override fun observeTeams(): Flow<List<Team>> {
        return local.observeTeams().map { teamEntities ->
            teamEntities.map { it.toTeam() }
        }
    }

    override fun observeVenues(): Flow<List<Venue>> {
        return local.observeVenues().map { venueEntities ->
            venueEntities.map { it.toVenue() }
        }
    }

    override fun observeCoaches(): Flow<List<Coach>> {
        return local.observeCoaches().map { coachEntities ->
            coachEntities.map { it.toCoach() }
        }
    }

    override suspend fun saveTeam(team: Team, leagueId: Int, season: Int) {
        local.saveTeam(team.toTeamEntity(leagueId, season))
    }

    override suspend fun saveVenue(venue: Venue) {
        local.saveVenue(venue.toVenueEntity())
    }

    override suspend fun saveCoach(coach: Coach) {
        local.saveCoach(coach.toCoachEntity())
    }

    override suspend fun saveTeams(teams: List<Team>) {
        local.saveTeams(teams.map { it.toTeamEntity() })
    }

    override suspend fun saveVenues(venues: List<Venue>) {
        local.saveVenues(venues.map { it.toVenueEntity() })
    }

    override suspend fun loadTeamInformation(
        teamId: Int,
        leagueId: Int,
        season: Int
    ): RepositoryResult<TeamWithVenue> {
        val result = remote.loadTeamInformation(teamId)
        return try {
            val teamResponseData = result.body()?.response?.firstOrNull()
            val team = teamResponseData?.team
            val venue = teamResponseData?.venue
            val teamWithVenue = TeamWithVenue(
                team = team?.toTeam(leagueId, season) ?: Team.EMPTY_TEAM,
                venue = venue?.toVenue(teamId) ?: Venue.EMPTY_VENUE
            )
            saveTeam(team?.toTeam(leagueId, season) ?: Team.EMPTY_TEAM, leagueId, season)
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

    override suspend fun loadTeamsByCountry(countryName: String): RepositoryResult<List<TeamWithVenue>> {
        val result = remote.loadTeamsByCountry(countryName)
        return try {
            val teamResponseData = result.body()?.response
            val teamWIthVenues = teamResponseData?.map {
                TeamWithVenue(
                    team = it.team.toTeam(),
                    venue = it.venue.toVenue(it.team.id)
                )
            }
            teamWIthVenues?.map {
                it.team.country
            }
            saveTeams(teamWIthVenues?.map { it.team }.orEmpty())
            saveVenues(teamWIthVenues?.map { it.venue }.orEmpty())
            RepositoryResult.Success(teamWIthVenues)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message()
                internalStatus = e.code()
            })
        }
    }
}