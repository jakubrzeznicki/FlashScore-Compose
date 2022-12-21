package com.kuba.flashscorecompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuba.flashscorecompose.data.country.local.CountryDao
import com.kuba.flashscorecompose.data.country.local.model.CountryEntity
import com.kuba.flashscorecompose.data.league.local.LeagueDao
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity

/**
 * Created by jrzeznicki on 9/5/2022
 */
@Database(entities = [CountryEntity::class, LeagueEntity::class], version = 7)
abstract class FlashScoreDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun leagueDao(): LeagueDao
}