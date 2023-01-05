package com.kuba.flashscorecompose.data.fixtures.currentround.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.fixtures.currentround.remote.model.CurrentRoundDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class CurrentRoundRemote(private val footballApi: FootballApi) : CurrentRoundRemoteDataSource {
    override suspend fun loadCurrentRound(
        leagueId: Int,
        season: Int,
        isCurrent: Boolean
    ): Response<CurrentRoundDataDto> =
        footballApi.getFixturesCurrentRound(
            leagueId = leagueId,
            season = season,
            isCurrent = isCurrent
        )
}