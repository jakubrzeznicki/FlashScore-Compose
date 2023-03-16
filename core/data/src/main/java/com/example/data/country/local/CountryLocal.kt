package com.example.data.country.local

import com.example.database.RoomStorage
import com.example.database.countries.model.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
class CountryLocal(private val roomStorage: RoomStorage) : CountryLocalDataSource {

    override fun observeCountries(countryNames: List<String>): Flow<List<CountryEntity>> {
        return roomStorage.getDatabase().countryDao().observeCountries(countryNames)
    }

    override suspend fun getCountry(countryName: String): CountryEntity? {
        return roomStorage.getDatabase().countryDao().getCountry(countryName)
    }

    override suspend fun getCountries(): List<CountryEntity> {
        return roomStorage.getDatabase().countryDao().getCountries()
    }

    override suspend fun saveCountries(countries: List<CountryEntity>) {
        roomStorage.getDatabase().countryDao().saveCountries(countries)
    }
}
