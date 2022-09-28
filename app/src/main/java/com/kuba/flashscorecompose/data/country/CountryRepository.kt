package com.kuba.flashscorecompose.data.country

import com.kuba.flashscorecompose.data.country.local.CountryLocalDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.country.remote.CountryRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Created by jrzeznicki on 9/7/2022
 */
class CountryRepository(
    private val local: CountryLocalDataSource,
    private val remote: CountryRemoteDataSource
) : CountryDataSource {
    override fun observeCountries(): Flow<List<Country>> = local.observeCountries()

    override fun saveCountries(countries: List<Country>) {
        local.saveCountries(countries)
    }

    override suspend fun loadCountries(): RepositoryResult<List<Country>> {
        return remote.loadCountries().also {
            withContext(Dispatchers.IO) {
                if (it is RepositoryResult.Success) {
                    local.deleteCountries()
                    local.saveCountries(it.data)
                }
            }
        }
    }
}