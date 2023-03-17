package com.kuba.flashscorecompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import com.example.explore.screen.destinations.ExploreRouteDestination
import com.example.fixturedetails.container.screen.destinations.FixtureDetailsRouteDestination
import com.example.home.screen.destinations.HomeScreenRouteDestination
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
import com.kuba.flashscorecompose.main.view.FlashScoreAppState
import com.kuba.flashscorecompose.main.view.navGraph
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

/**
 * Created by jrzeznicki on 16/03/2023.
 */
object NavGraphs {
    private const val ROOT = "root"
    private const val EXPLORE = "explore"
    private const val FIXTURE_DETAILS = "fixturedetails"
    private const val HOME = "home"
    private const val LEAGUE_DETAILS = "leaguedetails"
    private const val NOTIFICATIONS = "notifications"
    private const val ON_BOARDING = "onboarding"
    private const val PLAYER_DETAILS = "playerdetails"
    private const val PROFILE = "profile"
    private const val SIGN_IN = "signin"
    private const val STANDINGS = "standings"
    private const val STANDINGS_DETAILS = "standingsdetails"
    private const val TEAM_DETAILS = "teamdetails"
    private const val WELCOME = "welcome"
    val explore = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            ExploreRouteDestination,
            FixtureDetailsRouteDestination,
            TeamDetailsRouteDestination,
            PlayerDetailsRouteDestination,
            LeagueDetailsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = EXPLORE
        override val startRoute: Route = ExploreRouteDestination routedIn this
    }
    val fixtureDetails = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            FixtureDetailsRouteDestination,
            TeamDetailsRouteDestination,
            PlayerDetailsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = FIXTURE_DETAILS
        override val startRoute: Route = FixtureDetailsRouteDestination routedIn this
    }
    val home = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            HomeScreenRouteDestination,
            FixtureDetailsRouteDestination,
            LeagueDetailsRouteDestination,
            NotificationsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = HOME
        override val startRoute: Route = HomeScreenRouteDestination routedIn this
    }
    val leagueDetails = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            LeagueDetailsRouteDestination,
            FixtureDetailsRouteDestination,
            StandingsDetailsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = LEAGUE_DETAILS
        override val startRoute: Route = LeagueDetailsRouteDestination routedIn this
    }
    val notifications = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            NotificationsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = NOTIFICATIONS
        override val startRoute: Route = NotificationsRouteDestination routedIn this
    }
    val onBoarding = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            OnBoardingRouteDestination,
            HomeScreenRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = ON_BOARDING
        override val startRoute: Route = OnBoardingRouteDestination routedIn this
    }
    val playerDetails = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            PlayerDetailsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = PLAYER_DETAILS
        override val startRoute: Route = PlayerDetailsRouteDestination routedIn this
    }
    val profile = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            ProfileRouteDestination,
            SignInRouteDestination,
            SignUpRouteDestination,
            WelcomeRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = PROFILE
        override val startRoute: Route = ProfileRouteDestination routedIn this
    }
    val signIn = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SignInRouteDestination,
            OnBoardingRouteDestination,
            HomeScreenRouteDestination,
            WelcomeRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = SIGN_IN
        override val startRoute: Route = SignInRouteDestination routedIn this
    }
    val standings = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            StandingsRouteDestination,
            StandingsDetailsRouteDestination,
            LeagueDetailsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = STANDINGS
        override val startRoute: Route = StandingsRouteDestination routedIn this
    }
    val standingsDetails = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            StandingsDetailsRouteDestination,
            TeamDetailsRouteDestination,
            LeagueDetailsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = STANDINGS_DETAILS
        override val startRoute: Route = StandingsDetailsRouteDestination routedIn this
    }
    val teamDetails = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            TeamDetailsRouteDestination,
            PlayerDetailsRouteDestination,
            FixtureDetailsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = TEAM_DETAILS
        override val startRoute: Route = TeamDetailsRouteDestination routedIn this
    }
    val welcome = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            WelcomeRouteDestination,
            HomeScreenRouteDestination,
            SignInRouteDestination,
            SignUpRouteDestination,
            OnBoardingRouteDestination,
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = WELCOME
        override val startRoute: Route = WelcomeRouteDestination routedIn this
    }
    val root = object : NavGraphSpec {
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val route: String = ROOT
        override val startRoute: Route = SplashScreenDestination routedIn this
        override val nestedNavGraphs: List<NavGraphSpec> = listOf(
            explore,
            home,
            fixtureDetails,
            leagueDetails,
            notifications,
            onBoarding,
            playerDetails,
            profile,
            signIn,
            standingsDetails,
            standings,
            teamDetails,
            welcome
        )
    }

    fun ArrayDeque<NavBackStackEntry>.print(prefix: String = "stack") {
        val stack = toMutableList()
            .map { it.destination.route }
            .toTypedArray().contentToString()
        println("$prefix = $stack")
    }

    fun DestinationScope<*>.currentNavigator(): CommonNavGraphNavigator{
        return CommonNavGraphNavigator(
            navBackStackEntry.destination.navGraph(),
            navController
        )
    }

    @ExperimentalAnimationApi
    @Composable
    internal fun AppNavigation(
        modifier: Modifier = Modifier,
        appState: FlashScoreAppState
    ) {
        DestinationsNavHost(
            engine = appState.engine,
            navController = appState.navController,
            navGraph = NavGraphs.root,
            modifier = modifier,
            dependenciesContainerBuilder = {
                dependency(currentNavigator())
            }
        )
    }
}