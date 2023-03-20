package com.kuba.flashscorecompose.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kuba.flashscorecompose.account.service.DefaultLogService
import com.kuba.flashscorecompose.account.service.LogService
import com.kuba.flashscorecompose.data.LocalRoomStorage
import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.authentication.AuthenticationDataSource
import com.kuba.flashscorecompose.data.authentication.AuthenticationRepository
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.CountryRepository
import com.kuba.flashscorecompose.data.country.local.CountryLocal
import com.kuba.flashscorecompose.data.country.remote.CountryRemote
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
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.data.user.UserRepository
import com.kuba.flashscorecompose.data.user.local.UserLocal
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesRepository
import com.kuba.flashscorecompose.data.userpreferences.local.UserPreferencesLocal
import com.kuba.flashscorecompose.data.userpreferences.local.preferences.DefaultUserDataStore
import com.kuba.flashscorecompose.data.userpreferences.local.preferences.UserDataStore
import com.kuba.flashscorecompose.explore.viewmodel.ExploreViewModel
import com.kuba.flashscorecompose.fixturedetails.headtohead.viewmodel.HeadToHeadViewModel
import com.kuba.flashscorecompose.fixturedetails.lineup.viewmodel.LineupViewModel
import com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel.StatisticsViewModel
import com.kuba.flashscorecompose.home.viewmodel.HomeViewModel
import com.kuba.flashscorecompose.leaguedetails.viewmodel.LeagueDetailsViewModel
import com.kuba.flashscorecompose.network.uuidsource.UuidData
import com.kuba.flashscorecompose.network.uuidsource.UuidSource
import com.kuba.flashscorecompose.onboarding.viewmodel.OnBoardingViewModel
import com.kuba.flashscorecompose.playerdetails.viewmodel.PlayerDetailsViewModel
import com.kuba.flashscorecompose.profile.container.viewmodel.ProfileViewModel
import com.kuba.flashscorecompose.profile.details.viewmodel.ProfileDetailsViewModel
import com.kuba.flashscorecompose.profile.settings.viewmodel.ProfileSettingsViewModel
import com.kuba.flashscorecompose.signin.viewmodel.SignInViewModel
import com.kuba.flashscorecompose.signup.model.SignUpType
import com.kuba.flashscorecompose.signup.viewmodel.SignUpViewModel
import com.kuba.flashscorecompose.splash.viewmodel.SplashViewModel
import com.kuba.flashscorecompose.standings.viewmodel.StandingsViewModel
import com.kuba.flashscorecompose.standingsdetails.viewmodel.StandingsDetailsViewModel
import com.kuba.flashscorecompose.teamdetails.fixturesteam.viewmodel.FixturesTeamViewModel
import com.kuba.flashscorecompose.teamdetails.informations.viewmodel.TeamInformationsViewModel
import com.kuba.flashscorecompose.teamdetails.players.viewmodel.PlayersViewModel
import com.kuba.flashscorecompose.ui.component.snackbar.DefaultSnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
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
        viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
        viewModel { (fixtureId: Int, leagueId: Int, round: String, season: Int) ->
            StatisticsViewModel(fixtureId, leagueId, round, season, get(), get(), get(), get())
        }
        viewModel { (fixtureId: Int, leagueId: Int, season: Int) ->
            LineupViewModel(
                fixtureId,
                leagueId,
                season,
                get(),
                get()
            )
        }
        viewModel { (homeTeamId: Int, awayTeamId: Int, season: Int) ->
            HeadToHeadViewModel(
                homeTeamId,
                awayTeamId,
                season,
                get(),
                get()
            )
        }
        viewModel { StandingsViewModel(get(), get(), get(), get()) }
        viewModel { (leagueId: Int, season: Int) ->
            StandingsDetailsViewModel(
                leagueId,
                season,
                get(),
                get()
            )
        }
        viewModel { (league: League) -> LeagueDetailsViewModel(league, get(), get(), get()) }
        viewModel { (team: Team, leagueId: Int, season: Int) ->
            TeamInformationsViewModel(team, leagueId, season, get(), get(), get())
        }
        viewModel { (team: Team, season: Int) ->
            PlayersViewModel(
                team,
                season,
                get(),
                get(),
                get(),
                get()
            )
        }
        viewModel { (teamId: Int, season: Int) ->
            FixturesTeamViewModel(
                teamId,
                season,
                get(),
                get(),
                get()
            )
        }
        viewModel { (playerId: Int, team: Team, season: Int) ->
            PlayerDetailsViewModel(
                playerId,
                team,
                season,
                get(),
                get(),
                get()
            )
        }
        viewModel { ExploreViewModel(get(), get(), get(), get(), get(), get(), get()) }
        viewModel { SignInViewModel(get(), get(), get(), get()) }
        viewModel { (signUpType: SignUpType) -> SignUpViewModel(signUpType, get(), get()) }
        viewModel { WelcomeViewModel(get(), get(), get(), get()) }
        viewModel { SplashViewModel(get(), get()) }
        viewModel { OnBoardingViewModel(get(), get(), get(), get()) }
        viewModel { ProfileViewModel(get(), get(), get(), get()) }
        viewModel { ProfileDetailsViewModel(get(), get(), get(), get()) }
        viewModel { (userId: String) ->
            ProfileSettingsViewModel(
                userId,
                get(),
                get(),
                get(),
                get()
            )
        }
    }

    private val componentsModule = module {
        single<UuidSource> { UuidData() }
        single<LocalDate> { LocalDate.now() }
        single<SnackbarManager> { DefaultSnackbarManager() }
    }

    private val repositoryModule = module {
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
        single<AuthenticationDataSource> {
            val auth = Firebase.auth
            val storage = Firebase.storage
            AuthenticationRepository(auth, storage, get())
        }
        single<UserDataSource> {
            val local = UserLocal(get())
            UserRepository(local)
        }
        single<UserPreferencesDataSource> {
            val local = UserPreferencesLocal(get(), get())
            UserPreferencesRepository(local)
        }
    }

    private val storageModule = module {
        single<RoomStorage> {
            LocalRoomStorage(androidApplication())
        }
        single<UserDataStore> {
            DefaultUserDataStore(get())
        }
    }

    private val serviceModule = module {
        single<LogService> {
            DefaultLogService()
        }
    }

    fun getAllModules() = listOf(
        componentsModule,
        storageModule,
        dataStoreModule,
        networkModule,
        repositoryModule,
        viewModelsModule,
        serviceModule
    )
}