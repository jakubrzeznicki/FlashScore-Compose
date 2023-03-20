package com.kuba.flashscorecompose.data.team.information.remote

import com.kuba.flashscorecompose.data.team.information.remote.model.CoachDataDto
import com.kuba.flashscorecompose.data.team.information.remote.model.TeamDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 26/01/2023.
 */
interface TeamRemoteDataSource {
    suspend fun loadTeamInformation(teamId: Int): Response<TeamDataDto>
    suspend fun loadCoach(teamId: Int): Response<CoachDataDto>
    suspend fun loadTeamsByCountry(countryName: String): Response<TeamDataDto>
}
