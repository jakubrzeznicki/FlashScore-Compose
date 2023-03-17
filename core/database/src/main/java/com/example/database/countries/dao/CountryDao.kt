package com.example.database.countries.dao

import androidx.room.*
import com.example.database.countries.model.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
@Dao
interface CountryDao {
    @Query("SELECT * FROM countries WHERE name IN(:countryNames)")
    fun observeCountries(countryNames: List<String>): Flow<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE name = :countryName LIMIT 1")
    suspend fun getCountry(countryName: String): CountryEntity?

    @Query("SELECT * FROM countries")
    suspend fun getCountries(): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCountries(countries: List<CountryEntity>)
}