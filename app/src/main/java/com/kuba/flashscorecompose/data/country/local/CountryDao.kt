package com.kuba.flashscorecompose.data.country.local

import androidx.room.*
import com.kuba.flashscorecompose.data.country.local.model.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    fun observeCountries(): Flow<List<CountryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCountries(countries: List<CountryEntity>)

    @Query("DELETE from countries")
    fun deleteCountries()
}