package com.kuba.flashscorecompose.data.country.local

import com.kuba.flashscorecompose.data.country.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
interface CountryLocalDataSource {
    fun observeCountries(): Flow<List<Country>>
    fun saveCountries(countries: List<Country>)
    fun deleteCountries()
}