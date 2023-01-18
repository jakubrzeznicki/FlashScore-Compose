package com.kuba.flashscorecompose.data.standings.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.standings.remote.model.StandingsDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsRemote(private val footballApi: FootballApi) : StandingsRemoteDataSource {
    override suspend fun loadStandings(leagueId: Int, season: Int): Response<StandingsDataDto> =
        footballApi.getStandings(season = season, leagueId = leagueId)

}