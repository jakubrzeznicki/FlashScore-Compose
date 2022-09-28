package com.kuba.flashscorecompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuba.flashscorecompose.data.country.local.CountryDao
import com.kuba.flashscorecompose.data.country.model.Country

/**
 * Created by jrzeznicki on 9/5/2022
 */
@Database(entities = [Country::class], version = 1)
abstract class FlashScoreDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
}