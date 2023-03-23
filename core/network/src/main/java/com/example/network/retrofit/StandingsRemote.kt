package com.example.network.retrofit

import com.example.network.StandingsRemoteDataSource
import com.example.network.api.FootballApi
import com.example.network.model.standing.StandingsDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsRemote(private val footballApi: FootballApi) : StandingsRemoteDataSource {
    override suspend fun loadStandings(leagueId: Int, season: Int): Response<StandingsDataDto> =
        footballApi.getStandings(season = season, leagueId = leagueId)
}
