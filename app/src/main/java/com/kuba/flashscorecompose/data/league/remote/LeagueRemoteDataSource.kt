package com.kuba.flashscorecompose.data.league.remote

import com.kuba.flashscorecompose.data.league.remote.model.LeagueDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 10/3/2022
 */
interface LeagueRemoteDataSource {
    suspend fun loadLeagues(countryCode: String): Response<LeagueDataDto>
}