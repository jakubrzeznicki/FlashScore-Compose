package com.example.network

import com.example.network.model.country.CountryDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 9/9/2022
 */
interface CountryRemoteDataSource {
    suspend fun loadCountries(): Response<CountryDataDto>
}
