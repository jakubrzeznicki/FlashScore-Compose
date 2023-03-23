package com.example.network.retrofit

import com.example.network.TeamRemoteDataSource
import com.example.network.api.FootballApi
import com.example.network.model.team.CoachDataDto
import com.example.network.model.team.TeamDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 26/01/2023.
 */
class TeamRemote(private val footballApi: FootballApi) : TeamRemoteDataSource {
    override suspend fun loadTeamInformation(teamId: Int): Response<TeamDataDto> {
        val queryMap = mapOf(FootballApi.ID to teamId.toString())
        return footballApi.getTeams(queryMap)
    }

    override suspend fun loadCoach(teamId: Int): Response<CoachDataDto> =
        footballApi.getCoachByTeam(teamId = teamId)

    override suspend fun loadTeamsByCountry(countryName: String): Response<TeamDataDto> {
        val queryMap = mapOf(FootballApi.COUNTRY to countryName)
        return footballApi.getTeams(queryMap)
    }
}
