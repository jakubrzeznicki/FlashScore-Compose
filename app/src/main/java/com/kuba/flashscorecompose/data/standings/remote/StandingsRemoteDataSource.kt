package com.kuba.flashscorecompose.data.standings.remote

import com.kuba.flashscorecompose.data.standings.remote.model.StandingsDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 18/01/2023.
 */
interface StandingsRemoteDataSource {
    suspend fun loadStandings(leagueId: Int, season: Int): Response<StandingsDataDto>
}