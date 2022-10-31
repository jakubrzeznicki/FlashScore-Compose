package com.kuba.flashscorecompose.leagues.screen

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kuba.flashscorecompose.countries.model.CountriesUiState
import com.kuba.flashscorecompose.countries.screen.TopBar
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.leagues.model.LeaguesUiState
import com.kuba.flashscorecompose.leagues.viewmodel.LeaguesViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 10/3/2022
 */
private const val SETUP_LEAGUES_KEY = "SETUP_LEAGUES_KEY"

@Destination
@Composable
fun LeaguesListScreen(
    countryId: String,
    navigator: DestinationsNavigator,
    viewModel: LeaguesViewModel = getViewModel(parameters = { parametersOf(countryId) }),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_LEAGUES_KEY) { viewModel.setup() }
    LeaguesScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refreshLeagues() },
        onLeagueClick = {  },
        onErrorClear = { viewModel.cleanError() },
        scaffoldState = scaffoldState
    )
}

@Composable
fun LeaguesScreen(
    modifier: Modifier = Modifier,
    uiState: LeaguesUiState,
    onRefreshClick: () -> Unit,
    onLeagueClick: (League) -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState
) {
    Scaffold(topBar = { TopBar() }) {

    }
}