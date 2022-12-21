package com.kuba.flashscorecompose.data.country.remote

import com.kuba.flashscorecompose.data.country.remote.model.CountryDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 9/9/2022
 */
interface CountryRemoteDataSource {
    suspend fun loadCountries(): Response<CountryDataDto>
}