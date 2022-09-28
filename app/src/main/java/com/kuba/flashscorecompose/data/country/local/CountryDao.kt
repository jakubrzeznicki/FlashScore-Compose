package com.kuba.flashscorecompose.data.country.local

import androidx.room.*
import com.kuba.flashscorecompose.data.country.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    fun observeCountries(): Flow<List<Country>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveCountries(countries: List<Country>)

    @Query("DELETE from countries")
    fun deleteCountries()
}