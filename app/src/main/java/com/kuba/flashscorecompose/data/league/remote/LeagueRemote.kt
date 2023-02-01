package com.kuba.flashscorecompose.data.league.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.league.remote.model.LeagueDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 10/1/2022
 */
class LeagueRemote(private val footballApi: FootballApi) : LeagueRemoteDataSource {
    override suspend fun loadLeagues(countryCode: String): Response<LeagueDataDto> {
        val queryMap = mapOf(FootballApi.CODE to countryCode)
        return footballApi.getLeagues(queryMap)
    }

    override suspend fun loadLeague(leagueId: Int): Response<LeagueDataDto> {
        val queryMap = mapOf(FootballApi.ID to leagueId.toString())
        return footballApi.getLeagues(queryMap)
    }
}