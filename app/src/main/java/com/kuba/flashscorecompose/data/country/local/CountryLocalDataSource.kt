package com.kuba.flashscorecompose.data.country.local

import com.kuba.flashscorecompose.data.country.local.model.CountryEntity
import com.kuba.flashscorecompose.data.country.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
interface CountryLocalDataSource {
    fun observeCountries(): Flow<List<CountryEntity>>
    fun saveCountries(countries: List<CountryEntity>)
    fun deleteCountries()
}