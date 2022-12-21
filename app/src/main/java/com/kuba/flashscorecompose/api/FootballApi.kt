package com.kuba.flashscorecompose.api

import com.kuba.flashscorecompose.data.country.remote.model.CountryDataDto
import com.kuba.flashscorecompose.data.league.remote.model.LeagueDataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jrzeznicki on 9/5/2022
 */
interface FootballApi {

    @GET("$API_VERSION/$COUNTRIES")
    suspend fun getCountries(): Response<CountryDataDto>

    @GET("$API_VERSION/$LEAGUES")
    suspend fun getLeagues(@Query(CODE) countryCode: String): Response<LeagueDataDto>

    companion object {
        const val API_VERSION = "v3"
        const val COUNTRIES = "countries"
        const val LEAGUES = "leagues"
        const val CODE = "code"
    }
}