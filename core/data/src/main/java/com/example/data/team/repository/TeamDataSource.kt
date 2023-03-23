package com.example.data.team.repository

import com.example.common.utils.RepositoryResult
import com.example.model.team.Coach
import com.example.model.team.Team
import com.example.model.team.TeamWithVenue
import com.example.model.team.Venue
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 26/01/2023.
 */
interface TeamDataSource {
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
