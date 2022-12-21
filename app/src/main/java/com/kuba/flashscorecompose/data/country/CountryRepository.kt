package com.kuba.flashscorecompose.data.country

import android.util.Log
import com.kuba.flashscorecompose.data.country.local.CountryLocalDataSource
import com.kuba.flashscorecompose.data.country.mapper.toCountry
import com.kuba.flashscorecompose.data.country.mapper.toCountryEntity
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.country.remote.CountryRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 9/7/2022
 */
class CountryRepository(
    private val local: CountryLocalDataSource,
    private val remote: CountryRemoteDataSource
) : CountryDataSource {
    override fun observeCountries(): Flow<List<Country>> =
        local.observeCountries().map { countryEntity -> countryEntity.map { it.toCountry() } }


    override fun saveCountries(countries: List<Country>) {
        local.saveCountries(countries.map { it.toCountryEntity() })
    }

    override suspend fun loadCountries(): RepositoryResult<List<Country>> {
        val result = remote.loadCountries()
        return try {
            Log.d("TEST_LOG", "loadCountries success ${result.body()}")
            Log.d("TEST_LOG", "loadCountries success ${result.message()}")
            Log.d("TEST_LOG", "loadCountries success ${result.errorBody()}")
            Log.d("TEST_LOG", "loadCountries success ${result.headers()}")
            Log.d("TEST_LOG", "loadCountries success ${result.raw()}")
            Log.d("TEST_LOG", "loadCountries success ${result.code()}")
            val countries = result.body()?.countries?.map { it.toCountry() }
            withContext(Dispatchers.IO) {
                local.deleteCountries()
                local.saveCountries(countries?.map { it.toCountryEntity() }.orEmpty())
            }
            RepositoryResult.Success(countries)
        } catch (e: HttpException) {
            Log.d("TEST_LOG", "loadCountries error ${result.code()}")
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}