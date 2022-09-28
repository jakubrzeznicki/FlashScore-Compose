package com.kuba.flashscorecompose.data.country.remote

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.utils.RepositoryResult

/**
 * Created by jrzeznicki on 9/9/2022
 */
interface CountryRemoteDataSource {
    suspend fun loadCountries(): RepositoryResult<List<Country>>
}