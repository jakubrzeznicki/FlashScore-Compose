package com.kuba.flashscorecompose.data.country.local

import com.kuba.flashscorecompose.data.country.local.model.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
interface CountryLocalDataSource {
    suspend fun getCountry(countryName: String): CountryEntity?
    fun observeCountries(countryNames: List<String>): Flow<List<CountryEntity>>
    fun observeCountries(): Flow<List<CountryEntity>>
    suspend fun getCountries(): List<CountryEntity>
    fun saveCountries(countries: List<CountryEntity>)
    fun deleteCountries()
}