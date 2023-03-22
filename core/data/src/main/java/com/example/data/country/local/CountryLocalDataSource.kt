package com.example.data.country.local

import com.example.database.countries.model.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
interface CountryLocalDataSource {
    fun observeCountries(countryNames: List<String>): Flow<List<CountryEntity>>
    suspend fun getCountry(countryName: String): CountryEntity?
    suspend fun getCountries(): List<CountryEntity>
    suspend fun saveCountries(countries: List<CountryEntity>)
}
