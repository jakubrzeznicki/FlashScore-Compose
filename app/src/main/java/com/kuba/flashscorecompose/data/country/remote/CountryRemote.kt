package com.kuba.flashscorecompose.data.country.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.country.remote.model.CountryDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 9/7/2022
 */
class CountryRemote(private val footballApi: FootballApi) : CountryRemoteDataSource {

    override suspend fun loadCountries(): Response<CountryDataDto> = footballApi.getCountries()
}
