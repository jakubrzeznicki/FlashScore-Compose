package com.kuba.flashscorecompose.data.country.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.country.local.model.CountryEntity
import com.kuba.flashscorecompose.data.country.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 9/9/2022
 */
class CountryLocal(private val roomStorage: RoomStorage) : CountryLocalDataSource {
    override fun observeCountries(): Flow<List<CountryEntity>> {
        return roomStorage.getDatabase().countryDao().observeCountries()
    }

    override fun saveCountries(countries: List<CountryEntity>) {
        roomStorage.getDatabase().countryDao().saveCountries(countries)
    }

    override fun deleteCountries() {
        roomStorage.getDatabase().countryDao().deleteCountries()
    }
}