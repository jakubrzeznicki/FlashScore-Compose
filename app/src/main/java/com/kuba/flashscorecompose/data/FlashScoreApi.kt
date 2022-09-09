package com.kuba.flashscorecompose.data

import com.kuba.flashscorecompose.BuildConfig
import com.kuba.flashscorecompose.data.country.model.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jrzeznicki on 9/5/2022
 */
interface FlashScoreApi {

    @GET("$BASE_URL?$ACTION=$GET_COUNTRIES")
    suspend fun getCountries(
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY
    ): Response<CountryResponse>

    class CountryResponse : ArrayList<Country>()
    
    companion object {
        const val BASE_URL = "https://apiv3.apifootball.com"
        const val ACTION = "action"
        const val GET_COUNTRIES = "get_countries"
        const val API_KEY = "APIkey"
    }
}