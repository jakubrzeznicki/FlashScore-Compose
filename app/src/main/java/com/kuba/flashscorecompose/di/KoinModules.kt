package com.kuba.flashscorecompose.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kuba.flashscorecompose.account.service.AccountService
import com.kuba.flashscorecompose.account.service.DefaultAccountService
import com.kuba.flashscorecompose.account.service.DefaultLogService
import com.kuba.flashscorecompose.account.service.LogService
import com.kuba.flashscorecompose.countries.viewmodel.CountriesViewModel
import com.kuba.flashscorecompose.data.LocalRoomStorage
import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.CountryRepository
import com.kuba.flashscorecompose.data.country.local.CountryLocal
import com.kuba.flashscorecompose.data.country.remote.CountryRemote
import com.kuba.flashscorecompose.data.fixtures.currentround.CurrentRoundDataSource
import com.kuba.flashscorecompose.data.fixtures.currentround.CurrentRoundRepository
import com.kuba.flashscorecompose.data.fixtures.currentround.local.CurrentRoundLocal
import com.kuba.flashscorecompose.data.fixtures.currentround.remote.CurrentRoundRemote
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesRepository
import com.kuba.flashscorecompose.data.fixtures.fixture.local.FixtureLocal
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.FixtureRemote
import com.kuba.flashscorecompose.data.fixtures.lineups.LineupsDataSource
import com.kuba.flashscorecompose.data.fixtures.lineups.LineupsRepository
import com.kuba.flashscorecompose.data.fixtures.lineups.local.LineupLocal
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.LineupsRemote
import com.kuba.flashscorecompose.data.fixtures.statistics.StatisticsDataSource
import com.kuba.flashscorecompose.data.fixtures.statistics.StatisticsRepository
import com.kuba.flashscorecompose.data.fixtures.statistics.local.StatisticsLocal
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.StatisticsRemote
import com.kuba.flashscorecompose.data.league.LeagueDataSource
import com.kuba.flashscorecompose.data.league.LeagueRepository
import com.kuba.flashscorecompose.data.league.local.LeagueLocal
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.league.remote.LeagueRemote
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.data.players.PlayersRepository
import com.kuba.flashscorecompose.data.players.local.PlayersLocal
import com.kuba.flashscorecompose.data.players.remote.PlayersRemote
import com.kuba.flashscorecompose.data.standings.StandingsDataSource
import com.kuba.flashscorecompose.data.standings.StandingsRepository
import com.kuba.flashscorecompose.data.standings.local.StandingsLocal
import com.kuba.flashscorecompose.data.standings.remote.StandingsRemote
import com.kuba.flashscorecompose.data.team.information.TeamDataSource
import com.kuba.flashscorecompose.data.team.information.TeamRepository
import com.kuba.flashscorecompose.data.team.information.local.TeamLocal
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.remote.TeamRemote
import com.kuba.flashscorecompose.explore.viewmodel.ExploreViewModel
import com.kuba.flashscorecompose.fixturedetails.headtohead.viewmodel.HeadToHeadViewModel
import com.kuba.flashscorecompose.fixturedetails.lineup.viewmodel.LineupViewModel
import com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel.StatisticsViewModel
import com.kuba.flashscorecompose.home.viewmodel.HomeViewModel
import com.kuba.flashscorecompose.leaguedetails.viewmodel.LeagueDetailsViewModel
import com.kuba.flashscorecompose.leagues.viewmodel.LeaguesViewModel
import com.kuba.flashscorecompose.network.uuidsource.UuidData
import com.kuba.flashscorecompose.network.uuidsource.UuidSource
import com.kuba.flashscorecompose.playerdetails.viewmodel.PlayerDetailsViewModel
import com.kuba.flashscorecompose.signin.viewmodel.SignInViewModel
import com.kuba.flashscorecompose.signup.viewmodel.SignUpViewModel
import com.kuba.flashscorecompose.splash.viewmodel.SplashViewModel
import com.kuba.flashscorecompose.standings.viewmodel.StandingsViewModel
import com.kuba.flashscorecompose.standingsdetails.viewmodel.StandingsDetailsViewModel
import com.kuba.flashscorecompose.teamdetails.fixturesteam.viewmodel.FixturesTeamViewModel
import com.kuba.flashscorecompose.teamdetails.informations.viewmodel.TeamInformationsViewModel
import com.kuba.flashscorecompose.teamdetails.players.viewmodel.PlayersViewModel
import com.kuba.flashscorecompose.welcome.viewmodel.WelcomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDate

/**
 * Created by jrzeznicki on 9/5/2022
 */
class KoinModules {

    private val viewModelsModule = module {
        viewModel { HomeViewModel(get(), get(), get()) }
        viewModel { CountriesViewModel(get()) }
        viewModel { (countryCode: String) -> LeaguesViewModel(countryCode, get(), get()) }
        viewModel { (fixtureId: Int, leagueId: Int, round: String, season: Int) ->
            StatisticsViewModel(fixtureId, leagueId, round, season, get(), get())
        }
        viewModel { (fixtureId: Int) -> LineupViewModel(fixtureId, get()) }
        viewModel { (homeTeamId: Int, awayTeamId: Int, season: Int) ->
            HeadToHeadViewModel(
                homeTeamId,
                awayTeamId,
                season,
                get()
            )
        }
        viewModel { StandingsViewModel(get(), get(), get()) }
        viewModel { (leagueId: Int, season: Int) ->
            StandingsDetailsViewModel(
                leagueId,
                season,
                get()
            )
        }
        viewModel { (league: League) -> LeagueDetailsViewModel(league, get()) }
        viewModel { (team: Team, leagueId: Int, season: Int) ->
            TeamInformationsViewModel(team, leagueId, season, get(), get())
        }
        viewModel { (team: Team, season: Int) -> PlayersViewModel(team, season, get(), get()) }
        viewModel { (teamId: Int, season: Int) -> FixturesTeamViewModel(teamId, season, get()) }
        viewModel { (playerId: Int, team: Team, season: Int) ->
            PlayerDetailsViewModel(
                playerId,
                team,
                season,
                get()
            )
        }
        viewModel { ExploreViewModel(get(), get(), get(), get(), get()) }
        viewModel { SignInViewModel(get(), get()) }
        viewModel { SignUpViewModel(get(), get()) }
        viewModel { WelcomeViewModel(get(), get()) }
        viewModel { SplashViewModel(get(), get()) }
    }

    private val componentsModule = module {
        single<UuidSource> { UuidData() }
        single<LocalDate> { LocalDate.now() }
    }

    private val repositoryModule = module {
        single<CountryDataSource> {
            val local = CountryLocal(get())
            val remote = CountryRemote(get())
            CountryRepository(local, remote)
        }
        single<LeagueDataSource> {
            val local = LeagueLocal(get())
            val remote = LeagueRemote(get())
            LeagueRepository(local, remote)
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
        single<CurrentRoundDataSource> {
            val local = CurrentRoundLocal(get())
            val remote = CurrentRoundRemote(get())
            CurrentRoundRepository(local, remote)
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
    }

    private val storageModule = module {
        single<RoomStorage> {
            LocalRoomStorage(androidApplication())
        }
    }

    private val serviceModule = module {
        single<AccountService> {
            val auth = Firebase.auth
            DefaultAccountService(auth)
        }
        single<LogService> {
            DefaultLogService()
        }
    }

    fun getAllModules() = listOf(
        componentsModule,
        storageModule,
        networkModule,
        repositoryModule,
        viewModelsModule,
        serviceModule
    )
}