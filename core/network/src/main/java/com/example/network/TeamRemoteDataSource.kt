package com.example.network

import com.example.network.model.team.CoachDataDto
import com.example.network.model.team.TeamDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 26/01/2023.
 */
interface TeamRemoteDataSource {
    suspend fun loadTeamInformation(teamId: Int): Response<TeamDataDto>
    suspend fun loadCoach(teamId: Int): Response<CoachDataDto>
    suspend fun loadTeamsByCountry(countryName: String): Response<TeamDataDto>
}
