package com.kuba.flashscorecompose.data.team.information

import com.kuba.flashscorecompose.data.team.information.model.Coach
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.TeamWithVenue
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 26/01/2023.
 */
interface TeamDataSource {
    suspend fun getTeam(teamId: Int): Team?
    fun observeTeam(teamId: Int): Flow<Team?>
    fun observeVenue(teamId: Int): Flow<Venue?>
    fun observeCoach(teamId: Int): Flow<Coach?>
    fun observeTeams(): Flow<List<Team>>
    fun observeVenues(): Flow<List<Venue>>
    fun observeCoaches(): Flow<List<Coach>>
    suspend fun saveTeam(team: Team, leagueId: Int, season: Int)
    suspend fun saveVenue(venue: Venue)
    suspend fun saveCoach(coach: Coach)
    suspend fun saveTeams(teams: List<Team>)
    suspend fun saveVenues(venues: List<Venue>)
    suspend fun loadTeamInformation(
        teamId: Int,
        leagueId: Int,
        season: Int
    ): RepositoryResult<TeamWithVenue>

    suspend fun loadCoach(teamId: Int): RepositoryResult<Coach>
    suspend fun loadTeamsByCountry(countryName: String): RepositoryResult<List<TeamWithVenue>>
}