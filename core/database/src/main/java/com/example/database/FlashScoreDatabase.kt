package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.countries.dao.CountryDao
import com.example.database.countries.model.CountryEntity
import com.example.database.fixtures.lineups.converter.LineupsDataConverter
import com.example.database.fixtures.lineups.dao.LineupDao
import com.example.database.fixtures.lineups.model.LineupEntity
import com.example.database.fixtures.matches.dao.FixtureDao
import com.example.database.fixtures.matches.model.FixtureEntity
import com.example.database.fixtures.matches.model.FixtureInfoEntity
import com.example.database.fixtures.statistics.converter.StatisticsDataConverter
import com.example.database.fixtures.statistics.dao.StatisticsDao
import com.example.database.fixtures.statistics.model.StatisticsEntity
import com.example.database.leagues.dao.LeagueDao
import com.example.database.leagues.model.LeagueEntity
import com.example.database.notifications.dao.NotificationsDao
import com.example.database.notifications.model.NotificationDataEntity
import com.example.database.players.dao.PlayerDao
import com.example.database.players.model.PlayerEntity
import com.example.database.standings.converter.StandingsDataConverter
import com.example.database.standings.dao.StandingDao
import com.example.database.standings.model.StandingsEntity
import com.example.database.teams.dao.CoachDao
import com.example.database.teams.dao.TeamDao
import com.example.database.teams.dao.VenueDao
import com.example.database.teams.model.CoachEntity
import com.example.database.teams.model.TeamEntity
import com.example.database.teams.model.VenueEntity
import com.example.database.user.converter.UriConverter
import com.example.database.user.dao.UserDao
import com.example.database.user.model.UserEntity
import com.example.database.userpreferences.converter.UserPreferencesConverter
import com.example.database.userpreferences.dao.UserPreferencesDao
import com.example.database.userpreferences.model.UserPreferencesEntity


/**
 * Created by jrzeznicki on 9/5/2022
 */
@Database(
    entities = [
        CountryEntity::class, LeagueEntity::class, FixtureInfoEntity::class,
        TeamEntity::class, VenueEntity::class, CoachEntity::class, PlayerEntity::class,
        StatisticsEntity::class, LineupEntity::class, FixtureEntity::class, StandingsEntity::class,
        UserEntity::class, UserPreferencesEntity::class, NotificationDataEntity::class
    ],
    version = 1
)
@TypeConverters(
    StatisticsDataConverter::class,
    LineupsDataConverter::class,
    StandingsDataConverter::class,
    UserPreferencesConverter::class,
    UriConverter::class
)
abstract class FlashScoreDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun leagueDao(): LeagueDao
    abstract fun fixtureDao(): FixtureDao
    abstract fun lineupDao(): LineupDao
    abstract fun statisticsDao(): StatisticsDao
    abstract fun venueDao(): VenueDao
    abstract fun teamDao(): TeamDao
    abstract fun coachDao(): CoachDao
    abstract fun playerDao(): PlayerDao
    abstract fun standingDao(): StandingDao
    abstract fun userDao(): UserDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    abstract fun notificationsDao(): NotificationsDao
}
