package com.kuba.flashscorecompose.data.team.information.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.team.information.remote.model.CoachDataDto
import com.kuba.flashscorecompose.data.team.information.remote.model.TeamDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 26/01/2023.
 */
class TeamRemote(private val footballApi: FootballApi) : TeamRemoteDataSource {
    override suspend fun loadTeamInformation(teamId: Int): Response<TeamDataDto> =
        footballApi.getTeamInformation(teamId = teamId)

    override suspend fun loadCoach(teamId: Int): Response<CoachDataDto> =
        footballApi.getCoachByTeam(teamId = teamId)
}