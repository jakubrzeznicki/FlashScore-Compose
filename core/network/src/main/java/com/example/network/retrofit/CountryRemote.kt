package com.example.network.retrofit

import com.example.network.CountryRemoteDataSource
import com.example.network.api.FootballApi
import com.example.network.model.country.CountryDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 9/7/2022
 */
class CountryRemote(private val footballApi: FootballApi) : CountryRemoteDataSource {

    override suspend fun loadCountries(): Response<CountryDataDto> = footballApi.getCountries()
}
