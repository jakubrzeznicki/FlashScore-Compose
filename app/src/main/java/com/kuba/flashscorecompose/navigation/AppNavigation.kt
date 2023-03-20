package com.kuba.flashscorecompose.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
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
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.kuba.flashscorecompose.main.view.navGraph
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScopeWithNoDependencies
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
    private const val SPLASH = "splash"
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
        override val startRoute = ExploreRouteDestination routedIn this
    }

    //    val fixtureDetails = object : NavGraphSpec {
//        override val destinationsByRoute = listOf<DestinationSpec<*>>(
//            FixtureDetailsRouteDestination,
//            TeamDetailsRouteDestination,
//            PlayerDetailsRouteDestination
//        ).routedIn(this)
//            .associateBy { it.route }
//        override val route: String = FIXTURE_DETAILS
//        override val startRoute: Route = FixtureDetailsRouteDestination routedIn this
//    }
    val home = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            HomeDestination,
            FixtureDetailsRouteDestination,
            LeagueDetailsRouteDestination,
            NotificationsRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = HOME
        override val startRoute = HomeDestination routedIn this
    }

    //    val leagueDetails = object : NavGraphSpec {
//        override val destinationsByRoute = listOf<DestinationSpec<*>>(
//            LeagueDetailsRouteDestination,
//            FixtureDetailsRouteDestination,
//            StandingsDetailsRouteDestination
//        ).routedIn(this)
//            .associateBy { it.route }
//        override val route: String = LEAGUE_DETAILS
//        override val startRoute: Route = LeagueDetailsRouteDestination routedIn this
//    }
//    val notifications = object : NavGraphSpec {
//        override val destinationsByRoute = listOf<DestinationSpec<*>>(
//            NotificationsRouteDestination
//        ).routedIn(this)
//            .associateBy { it.route }
//        override val route: String = NOTIFICATIONS
//        override val startRoute: Route = NotificationsRouteDestination routedIn this
//    }
    val onBoarding = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            OnBoardingRouteDestination,
            HomeDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = ON_BOARDING
        override val startRoute = OnBoardingRouteDestination routedIn this
    }

    //    val playerDetails = object : NavGraphSpec {
//        override val destinationsByRoute = listOf<DestinationSpec<*>>(
//            PlayerDetailsRouteDestination
//        ).routedIn(this)
//            .associateBy { it.route }
//        override val route: String = PLAYER_DETAILS
//        override val startRoute: Route = PlayerDetailsRouteDestination routedIn this
//    }
    val profile = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            ProfileRouteDestination,
            SignInRouteDestination,
            SignUpRouteDestination,
            WelcomeRouteDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = PROFILE
        override val startRoute = ProfileRouteDestination routedIn this
    }

    //    val signIn = object : NavGraphSpec {
//        override val destinationsByRoute = listOf<DestinationSpec<*>>(
//            SignInRouteDestination,
//            OnBoardingRouteDestination,
//            HomeScreenRouteDestination,
//            WelcomeRouteDestination
//        ).routedIn(this)
//            .associateBy { it.route }
//        override val route: String = SIGN_IN
//        override val startRoute: Route = SignInRouteDestination routedIn this
//    }
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

    //    val standingsDetails = object : NavGraphSpec {
//        override val destinationsByRoute = listOf<DestinationSpec<*>>(
//            StandingsDetailsRouteDestination,
//            TeamDetailsRouteDestination,
//            LeagueDetailsRouteDestination
//        ).routedIn(this)
//            .associateBy { it.route }
//        override val route: String = STANDINGS_DETAILS
//        override val startRoute: Route = StandingsDetailsRouteDestination routedIn this
//    }
//    val teamDetails = object : NavGraphSpec {
//        override val destinationsByRoute = listOf<DestinationSpec<*>>(
//            TeamDetailsRouteDestination,
//            PlayerDetailsRouteDestination,
//            FixtureDetailsRouteDestination
//        ).routedIn(this)
//            .associateBy { it.route }
//        override val route: String = TEAM_DETAILS
//        override val startRoute: Route = TeamDetailsRouteDestination routedIn this
//    }
    val welcome = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            WelcomeRouteDestination,
            //HomeScreenRouteDestination,
            SignInRouteDestination,
            SignUpRouteDestination,
            OnBoardingRouteDestination,
        ).associateBy { it.route }
        override val route: String = WELCOME
        override val startRoute = WelcomeRouteDestination
    }
    val splash = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SplashScreenDestination,
            WelcomeRouteDestination,
            HomeDestination
        ).routedIn(this)
            .associateBy { it.route }
        override val route: String = SPLASH
        override val startRoute = SplashScreenDestination routedIn this
    }
    val root = object : NavGraphSpec {
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SplashScreenDestination,
            WelcomeRouteDestination,
            HomeDestination
        ).associateBy { it.route }
        override val route: String = "root"
        override val startRoute = SplashScreenDestination
        override val nestedNavGraphs = listOf(
//            SplashScreenDestination,
            //welcome,
            //onBoarding,
            //signIn,
            home,
            explore,
//            fixtureDetails,
//            leagueDetails,
//            notifications,
//            playerDetails,
            profile,
            //standingsDetails,
            standings,
            //teamDetails,
        )
    }

    fun ArrayDeque<NavBackStackEntry>.print(prefix: String = "stack") {
        val stack = toMutableList()
            .map { it.destination.route }
            .toTypedArray().contentToString()
        println("$prefix = $stack")
    }

    fun DestinationScopeWithNoDependencies<*>.currentNavigator(): CommonNavGraphNavigator {
        return CommonNavGraphNavigator(
            navBackStackEntry.destination.navGraph(),
            navController
        )
    }

    @OptIn(ExperimentalMaterialNavigationApi::class)
    @ExperimentalAnimationApi
    @Composable
    internal fun AppNavigation(
        modifier: Modifier = Modifier,
        navController: NavHostController
    ) {
        DestinationsNavHost(
            engine = rememberAnimatedNavHostEngine(
                rootDefaultAnimations = RootNavGraphDefaultAnimations(
                    enterTransition = { defaultTiviEnterTransition(initialState, targetState) },
                    exitTransition = { defaultTiviExitTransition(initialState, targetState) },
                    popEnterTransition = { defaultTiviPopEnterTransition() },
                    popExitTransition = { defaultTiviPopExitTransition() },
                )
            ),
            navController = navController,
            navGraph = root,
            modifier = modifier,
            dependenciesContainerBuilder = {
                dependency(currentNavigator())
            }
        )
    }

    @ExperimentalAnimationApi
    private fun AnimatedContentScope<*>.defaultTiviEnterTransition(
        initial: NavBackStackEntry,
        target: NavBackStackEntry,
    ): EnterTransition {
        val initialNavGraph = initial.destination.hostNavGraph
        val targetNavGraph = target.destination.hostNavGraph
        // If we're crossing nav graphs (bottom navigation graphs), we crossfade
        if (initialNavGraph.id != targetNavGraph.id) {
            return fadeIn()
        }
        // Otherwise we're in the same nav graph, we can imply a direction
        return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
    }

    @ExperimentalAnimationApi
    private fun AnimatedContentScope<*>.defaultTiviExitTransition(
        initial: NavBackStackEntry,
        target: NavBackStackEntry,
    ): ExitTransition {
        val initialNavGraph = initial.destination.hostNavGraph
        val targetNavGraph = target.destination.hostNavGraph
        // If we're crossing nav graphs (bottom navigation graphs), we crossfade
        if (initialNavGraph.id != targetNavGraph.id) {
            return fadeOut()
        }
        // Otherwise we're in the same nav graph, we can imply a direction
        return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
    }

    private val NavDestination.hostNavGraph: NavGraph
        get() = hierarchy.first { it is NavGraph } as NavGraph

    @ExperimentalAnimationApi
    private fun AnimatedContentScope<*>.defaultTiviPopEnterTransition(): EnterTransition {
        return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
    }

    @ExperimentalAnimationApi
    private fun AnimatedContentScope<*>.defaultTiviPopExitTransition(): ExitTransition {
        return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
    }
}