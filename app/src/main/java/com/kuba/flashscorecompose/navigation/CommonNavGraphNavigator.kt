package com.kuba.flashscorecompose.navigation

import androidx.navigation.NavController
import com.example.data.navigation.*
import com.example.explore.navigation.ExploreNavigator
import com.example.fixturedetails.container.screen.destinations.FixtureDetailsRouteDestination
import com.example.fixturedetails.navigation.FixtureDetailsNavigator
import com.example.home.navigation.HomeNavigator
import com.example.home.screen.destinations.HomeDestination
import com.example.leaguedetails.navigation.LeagueDetailsNavigator
import com.example.leaguedetails.screen.destinations.LeagueDetailsRouteDestination
import com.example.model.league.League
import com.example.model.team.Team
import com.example.notifications.navigation.NotificationsNavigator
import com.example.notifications.screen.destinations.NotificationsRouteDestination
import com.example.onboarding.navigation.OnBoardingNavigator
import com.example.onboarding.screen.destinations.OnBoardingRouteDestination
import com.example.playerdetails.navigation.PlayerDetailsNavigator
import com.example.playerdetails.screen.destinations.PlayerDetailsRouteDestination
import com.example.profile.container.screen.destinations.ProfileRouteDestination
import com.example.profile.navigation.ProfileNavigator
import com.example.signin.destinations.SignInRouteDestination
import com.example.signin.destinations.SignUpRouteDestination
import com.example.signin.navigation.SignInNavigator
import com.example.standings.navigation.StandingsNavigator
import com.example.standingsdetails.navigation.StandingsDetailsNavigator
import com.example.standingsdetails.screen.destinations.StandingsDetailsRouteDestination
import com.example.teamdetails.container.destinations.TeamDetailsRouteDestination
import com.example.teamdetails.navigation.TeamDetailsNavigator
import com.example.welcome.destinations.SplashScreenDestination
import com.example.welcome.destinations.WelcomeRouteDestination
import com.example.welcome.navigation.WelcomeNavigator
import com.ramcosta.composedestinations.navigation.navigate

/**
 * Created by jrzeznicki on 16/03/2023.
 */
class CommonNavGraphNavigator(private val navController: NavController) : ExploreNavigator,
    FixtureDetailsNavigator,
    HomeNavigator,
    LeagueDetailsNavigator,
    NotificationsNavigator,
    OnBoardingNavigator,
    PlayerDetailsNavigator,
    ProfileNavigator,
    SignInNavigator,
    StandingsNavigator,
    StandingsDetailsNavigator,
    TeamDetailsNavigator,
    WelcomeNavigator {

    override fun openTeamDetails(team: Team, leagueId: Int, season: Int) {
        navController.navigate(TeamDetailsRouteDestination(team, leagueId, season))
    }

    override fun openLeagueDetails(league: League) {
        navController.navigate(LeagueDetailsRouteDestination(league))
    }

    override fun openPlayerDetails(playerId: Int, team: Team, season: Int) {
        navController.navigate(
            PlayerDetailsRouteDestination(
                playerId,
                team,
                season
            )
        )
    }

    override fun openFixtureDetails(fixtureId: Int) {
        navController.navigate(FixtureDetailsRouteDestination(fixtureId))
    }

    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun openStandingsDetails(league: League) {
        navController.navigate(StandingsDetailsRouteDestination(league))
    }

    override fun openNotifications() {
        navController.navigate(NotificationsRouteDestination())
    }

    override fun openOnBoarding(onBoardingBackStackType: OnBoardingBackStackType) {
        navController.navigate(OnBoardingRouteDestination()) {
            when (onBoardingBackStackType) {
                is OnBoardingBackStackType.SignIn ->
                    popUpTo(SignInRouteDestination.route) { inclusive = true }
            }
        }
    }

    override fun openHome(homeBackStackType: HomeBackStackType) {
        navController.navigate(HomeDestination) {
            when (homeBackStackType) {
                is HomeBackStackType.Splash ->
                    popUpTo(SplashScreenDestination.route) { inclusive = true }
                is HomeBackStackType.SignIn ->
                    popUpTo(SignInRouteDestination.route) { inclusive = true }
                is HomeBackStackType.OnBoarding ->
                    popUpTo(OnBoardingRouteDestination.route) { inclusive = true }
                is HomeBackStackType.Welcome ->
                    popUpTo(WelcomeRouteDestination.route) { inclusive = true }
            }
        }
    }

    override fun openSignIn(signInBackStackType: SignInBackStackType) {
        when (signInBackStackType) {
            SignInBackStackType.Profile ->
                navController.navigate(SignInRouteDestination) {
                    popUpTo(ProfileRouteDestination.route) { inclusive = true }
                }
            SignInBackStackType.Welcome ->
                navController.navigate(SignInRouteDestination)
        }
    }

    override fun openWelcome(welcomeBackStackType: WelcomeBackStackType) {
        navController.navigate(WelcomeRouteDestination) {
            when (welcomeBackStackType) {
                is WelcomeBackStackType.Profile ->
                    popUpTo(ProfileRouteDestination.route) { inclusive = true }
                is WelcomeBackStackType.Splash ->
                    popUpTo(SplashScreenDestination.route) { inclusive = true }
                is WelcomeBackStackType.SignUp ->
                    popUpTo(SignUpRouteDestination.route) { inclusive = true }
            }
        }
    }

    override fun openSignUp(signUpType: SignUpType) {
        when (signUpType) {
            SignUpType.Anonymous ->
                navController.navigate(SignUpRouteDestination(signUpType)) {
                    popUpTo(ProfileRouteDestination.route) { inclusive = true }
                }
            SignUpType.New ->
                navController.navigate(SignUpRouteDestination(signUpType))
        }
    }

    override fun openOnBoarding() {
        navController.navigate(OnBoardingRouteDestination())
    }
}