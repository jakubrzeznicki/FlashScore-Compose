package com.kuba.flashscorecompose.data.country

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/7/2022
 */
interface CountryDataSource {
    fun observeCountries(countryCodes: List<String>): Flow<List<Country>>
    fun saveCountries(countries: List<Country>)
    suspend fun loadCountries(): RepositoryResult<List<Country>>
}