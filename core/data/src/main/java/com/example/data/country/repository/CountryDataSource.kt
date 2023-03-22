package com.example.data.country.repository

import com.example.common.utils.RepositoryResult
import com.example.model.country.Country
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/7/2022
 */
interface CountryDataSource {
    fun observeCountries(countryNames: List<String>): Flow<List<Country>>
    suspend fun getCountry(countryName: String): Country?
    suspend fun getCountries(): List<Country>
    suspend fun saveCountries(countries: List<Country>)
    suspend fun loadCountries(): RepositoryResult<List<Country>>
}
