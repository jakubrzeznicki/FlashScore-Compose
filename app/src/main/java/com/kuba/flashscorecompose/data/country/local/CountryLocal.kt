package com.kuba.flashscorecompose.data.country.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.country.local.model.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
class CountryLocal(private val roomStorage: RoomStorage) : CountryLocalDataSource {

    override fun observeCountries(countryNames: List<String>): Flow<List<CountryEntity>> {
        return roomStorage.getDatabase().countryDao().observeCountries(countryNames)
    }

    override fun observeCountries(): Flow<List<CountryEntity>> {
        return roomStorage.getDatabase().countryDao().observeCountries()
    }

    override fun observeCountry(countryName: String): Flow<CountryEntity?> {
        return roomStorage.getDatabase().countryDao().observeCountry(countryName)
    }

    override suspend fun getCountry(countryName: String): CountryEntity? {
        return roomStorage.getDatabase().countryDao().getCountry(countryName)
    }

    override suspend fun getCountries(): List<CountryEntity> {
        return roomStorage.getDatabase().countryDao().getCountries()
    }

    override fun saveCountries(countries: List<CountryEntity>) {
        roomStorage.getDatabase().countryDao().saveCountries(countries)
    }

    override fun deleteCountries() {
        roomStorage.getDatabase().countryDao().deleteCountries()
    }
}