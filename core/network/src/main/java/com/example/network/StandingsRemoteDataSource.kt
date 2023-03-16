package com.example.network

import com.example.network.model.standing.StandingsDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 18/01/2023.
 */
interface StandingsRemoteDataSource {
    suspend fun loadStandings(leagueId: Int, season: Int): Response<StandingsDataDto>
}
