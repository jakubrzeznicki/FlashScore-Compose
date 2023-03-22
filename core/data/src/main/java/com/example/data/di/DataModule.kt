package com.example.data.di

import com.example.data.country.local.CountryLocal
import com.example.data.country.repository.CountryDataSource
import com.example.data.country.repository.CountryRepository
import com.example.data.fixture.local.FixtureLocal
import com.example.data.fixture.repository.FixturesDataSource
import com.example.data.fixture.repository.FixturesRepository
import com.example.data.league.local.LeagueLocal
import com.example.data.league.repository.LeagueDataSource
import com.example.data.league.repository.LeagueRepository
import com.example.data.lineup.local.LineupLocal
import com.example.data.lineup.repository.LineupsDataSource
import com.example.data.lineup.repository.LineupsRepository
import com.example.data.notification.local.NotificationsLocal
import com.example.data.notification.repository.NotificationsDataSource
import com.example.data.notification.repository.NotificationsRepository
import com.example.data.player.local.PlayersLocal
import com.example.data.player.repository.PlayersDataSource
import com.example.data.player.repository.PlayersRepository
import com.example.data.standings.local.StandingsLocal
import com.example.data.standings.repository.StandingsDataSource
import com.example.data.standings.repository.StandingsRepository
import com.example.data.statistics.local.StatisticsLocal
import com.example.data.statistics.repository.StatisticsDataSource
import com.example.data.statistics.repository.StatisticsRepository
import com.example.data.team.local.TeamLocal
import com.example.data.team.repository.TeamDataSource
import com.example.data.team.repository.TeamRepository
import com.example.data.user.local.UserLocal
import com.example.data.user.repository.UserDataSource
import com.example.data.user.repository.UserRepository
import com.example.data.userpreferences.local.UserPreferencesLocal
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.data.userpreferences.repository.UserPreferencesRepository
import com.example.network.retrofit.*
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 14/03/2023.
 */
val repositoryModule = module {
    single<CountryDataSource> {
        val local = CountryLocal(get())
        val remote = CountryRemote(get())
        CountryRepository(local, remote)
    }
    single<LeagueDataSource> {
        val local = LeagueLocal(get())
        LeagueRepository(local)
    }
    single<FixturesDataSource> {
        val local = FixtureLocal(get())
        val remote = FixtureRemote(get())
        FixturesRepository(local, remote)
    }
    single<LineupsDataSource> {
        val local = LineupLocal(get())
        val remote = LineupsRemote(get())
        LineupsRepository(local, remote)
    }
    single<StatisticsDataSource> {
        val local = StatisticsLocal(get())
        val remote = StatisticsRemote(get())
        StatisticsRepository(local, remote)
    }
    single<StandingsDataSource> {
        val local = StandingsLocal(get())
        val remote = StandingsRemote(get())
        StandingsRepository(local, remote)
    }
    single<TeamDataSource> {
        val local = TeamLocal(get())
        val remote = TeamRemote(get())
        TeamRepository(local, remote)
    }
    single<PlayersDataSource> {
        val local = PlayersLocal(get())
        val remote = PlayersRemote(get())
        PlayersRepository(local, remote)
    }
    single<UserDataSource> {
        val local = UserLocal(get())
        UserRepository(local)
    }
    single<UserPreferencesDataSource> {
        val local = UserPreferencesLocal(get(), get())
        UserPreferencesRepository(local)
    }
    single<NotificationsDataSource> {
        val local = NotificationsLocal(get())
        NotificationsRepository(local)
    }
}