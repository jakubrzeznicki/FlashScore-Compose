package com.kuba.flashscorecompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.explore.screen.destinations.ExploreRouteDestination
import com.example.fixturedetails.container.screen.destinations.FixtureDetailsRouteDestination
import com.example.home.screen.destinations.HomeDestination
import com.example.leaguedetails.screen.destinations.LeagueDetailsRouteDestination
import com.example.notifications.screen.destinations.NotificationsRouteDestination
import com.example.onboarding.screen.destinations.OnBoardingRouteDestination
import com.example.playerdetails.screen.destinations.PlayerDetailsRouteDestination
import com.example.profile.container.screen.destinations.ProfileRouteDestination
import com.example.signin.destinations.SignInRouteDestination
import com.example.signin.destinations.SignUpRouteDestination
import com.example.standings.screen.destinations.StandingsRouteDestination
import com.example.standingsdetails.screen.destinations.StandingsDetailsRouteDestination
import com.example.teamdetails.container.destinations.TeamDetailsRouteDestination
import com.example.welcome.destinations.SplashScreenDestination
import com.example.welcome.destinations.WelcomeRouteDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScopeWithNoDependencies
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.spec.Route

/**
 * Created by jrzeznicki on 16/03/2023.
 */
object NavGraphs {
    private const val ROOT = "root"
    private const val EXPLORE = "explore"
    private const val HOME = "homee"
    private const val ON_BOARDING = "onboarding"
    private const val PROFILE = "profile"
    private const val STANDINGS = "standings"
    private const val WELCOME = "welcome"
    val explore = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            ExploreRouteDestination,
            TeamDetailsRouteDestination,
            PlayerDetailsRouteDestination
        ).associateBy { it.route }
        override val route: String = EXPLORE
        override val startRoute = ExploreRouteDestination
    }
    val home = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            HomeDestination,
            FixtureDetailsRouteDestination,
            NotificationsRouteDestination
        ).associateBy { it.route }
        override val route: String = HOME
        override val startRoute = HomeDestination
    }
    val onBoarding = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            OnBoardingRouteDestination
        ).associateBy { it.route }
        override val route: String = ON_BOARDING
        override val startRoute = OnBoardingRouteDestination
    }
    val profile = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            ProfileRouteDestination,
            SignInRouteDestination,
            SignUpRouteDestination,
            WelcomeRouteDestination
        ).associateBy { it.route }
        override val route: String = PROFILE
        override val startRoute = ProfileRouteDestination
    }
    val standings = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            StandingsRouteDestination,
            StandingsDetailsRouteDestination,
            LeagueDetailsRouteDestination
        ).associateBy { it.route }
        override val route: String = STANDINGS
        override val startRoute: Route = StandingsRouteDestination
    }
    val welcome = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            WelcomeRouteDestination,
            HomeDestination,
            SignInRouteDestination,
            SignUpRouteDestination,
            OnBoardingRouteDestination,
        ).associateBy { it.route }
        override val route: String = WELCOME
        override val startRoute = WelcomeRouteDestination
    }
    val root = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SplashScreenDestination
        ).associateBy { it.route }
        override val route: String = ROOT
        override val startRoute = SplashScreenDestination
        override val nestedNavGraphs = listOf(
            welcome,
            onBoarding,
            home,
            explore,
            profile,
            standings
        )
    }

    private fun DestinationScopeWithNoDependencies<*>.currentNavigator(): CommonNavGraphNavigator {
        return CommonNavGraphNavigator(navController)
    }

    @ExperimentalAnimationApi
    @Composable
    internal fun AppNavigation(
        modifier: Modifier = Modifier,
        engine: NavHostEngine,
        navController: NavHostController
    ) {
        DestinationsNavHost(
            engine = engine,
            navController = navController,
            navGraph = root,
            modifier = modifier,
            dependenciesContainerBuilder = {
                dependency(currentNavigator())
            }
        )
    }
}