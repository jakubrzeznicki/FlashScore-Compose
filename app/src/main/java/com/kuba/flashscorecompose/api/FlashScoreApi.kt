package com.kuba.flashscorecompose.api

import com.kuba.flashscorecompose.BuildConfig
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.league.model.League
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by jrzeznicki on 9/5/2022
 */
interface FlashScoreApi {

    @GET("$BASE_URL?$ACTION=$GET_COUNTRIES")
    suspend fun getCountries(
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY
    ): Response<CountryResponse>

    @GET("$BASE_URL?$ACTION=$GET_LEAGUES&")
    suspend fun getLeagues(
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY,
        @Path(COUNTRY_ID) countryId: String
    ): Response<LeagueResponse>

    class CountryResponse : ArrayList<Country>()

    class LeagueResponse : ArrayList<League>()

    companion object {
        const val BASE_URL = "https://apiv3.apifootball.com"
        const val ACTION = "action"
        const val GET_COUNTRIES = "get_countries"
        const val GET_LEAGUES = "get_leagues"
        const val COUNTRY_ID = "country_id"
        const val API_KEY = "APIkey"
    }
}