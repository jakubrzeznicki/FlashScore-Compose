package com.kuba.flashscorecompose.data.country

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.utils.RepositoryResult

/**
 * Created by jrzeznicki on 9/7/2022
 */
class CountryRepository(
    private val local: CountryDataSource,
    private val remote: CountryDataSource
) : CountryDataSource {
    override suspend fun loadCountries(): RepositoryResult<List<Country>> {
        return remote.loadCountries().also {
            if (it is RepositoryResult.Success) local.saveCountries(it.data)
        }
    }
}