package com.kuba.flashscorecompose.data.country

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.utils.LiveResult
import com.kuba.flashscorecompose.utils.RepositoryResult

/**
 * Created by jrzeznicki on 9/7/2022
 */
interface CountryDataSource {
    fun getCountries(): LiveResult<List<Country>> {
        throw NotImplementedError(this::class.java.name)
    }

    fun saveCountries(countries: List<Country>) {
        throw NotImplementedError(this::class.java.name)
    }

    suspend fun loadCountries(): RepositoryResult<List<Country>> {
        throw NotImplementedError(this::class.java.name)
    }
}