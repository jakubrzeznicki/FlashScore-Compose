package com.kuba.flashscorecompose.data.country.remote

import com.kuba.flashscorecompose.data.FlashScoreApi
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 9/7/2022
 */
class CountryRemote(private val flashScoreApi: FlashScoreApi) : CountryDataSource {

    override suspend fun loadCountries(): RepositoryResult<List<Country>> {
        val result =  flashScoreApi.getCountries()
       return if (result.isSuccessful) {
            RepositoryResult.Success(result.body().orEmpty())
        } else {
            RepositoryResult.Error(ResponseStatus())
       }
    }
}