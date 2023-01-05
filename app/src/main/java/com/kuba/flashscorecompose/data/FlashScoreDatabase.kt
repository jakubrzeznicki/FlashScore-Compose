package com.kuba.flashscorecompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kuba.flashscorecompose.data.country.local.CountryDao
import com.kuba.flashscorecompose.data.country.local.model.CountryEntity
import com.kuba.flashscorecompose.data.fixtures.currentround.local.CurrentRoundDao
import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.FixtureDao
import com.kuba.flashscorecompose.data.fixtures.fixture.local.TeamDao
import com.kuba.flashscorecompose.data.fixtures.fixture.local.VenueDao
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.FixtureInfoEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.LeagueFixtureEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.TeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.VenueEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.converter.LineupsDataConverter
import com.kuba.flashscorecompose.data.fixtures.statistics.converter.StatisticsDataConverter
import com.kuba.flashscorecompose.data.fixtures.lineups.local.LineupDao
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.CoachEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.LineupEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.PlayerEntity
import com.kuba.flashscorecompose.data.fixtures.statistics.local.StatisticsDao
import com.kuba.flashscorecompose.data.fixtures.statistics.local.model.StatisticsEntity
import com.kuba.flashscorecompose.data.league.local.LeagueDao
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity

/**
 * Created by jrzeznicki on 9/5/2022
 */
@Database(
    entities = [CountryEntity::class, LeagueEntity::class, CurrentRoundEntity::class,
        FixtureInfoEntity::class, LeagueFixtureEntity::class, TeamEntity::class, VenueEntity::class,
        CoachEntity::class, PlayerEntity::class, StatisticsEntity::class, LineupEntity::class],
    version = 7
)
@TypeConverters(StatisticsDataConverter::class, LineupsDataConverter::class)
abstract class FlashScoreDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun leagueDao(): LeagueDao
    abstract fun currentRound(): CurrentRoundDao
    abstract fun fixtureDao(): FixtureDao
    abstract fun lineupDao(): LineupDao
    abstract fun statisticsDao(): StatisticsDao
    abstract fun venueDao(): VenueDao
    abstract fun teamDao(): TeamDao
}