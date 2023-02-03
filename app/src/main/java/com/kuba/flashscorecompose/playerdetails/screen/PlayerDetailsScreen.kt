package com.kuba.flashscorecompose.playerdetails.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.playerdetails.model.PlayerDetailsError
import com.kuba.flashscorecompose.playerdetails.model.PlayerDetailsUiState
import com.kuba.flashscorecompose.playerdetails.viewmodel.PlayerDetailsViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 01/02/2023.
 */
private const val SETUP_PLAYER_DETAILS_KEY = "SETUP_PLAYER_DETAILS_KEY"

@Destination
@Composable
fun PlayerDetailsRoute(
    playerId: Int,
    countryLogo: String,
    team: Team,
    navigator: DestinationsNavigator,
    viewModel: PlayerDetailsViewModel = getViewModel { parametersOf(playerId, countryLogo) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = SETUP_PLAYER_DETAILS_KEY) { viewModel.setup() }
    PlayerDetailsScreen(
        uiState = uiState,
        team = team,
        countryLogo = countryLogo,
        snackbarHostState = snackbarHostState,
        navigator = navigator,
        onErrorClear = { viewModel.cleanError() },
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailsScreen(
    uiState: PlayerDetailsUiState,
    team: Team,
    countryLogo: String,
    snackbarHostState: SnackbarHostState,
    navigator: DestinationsNavigator,
    onErrorClear: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(navigator) },
        snackbarHost = { FlashScoreSnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 16.dp, end =  16.dp)
        ) {
            when (uiState) {
                is PlayerDetailsUiState.HasData -> {
                    HeaderDetailsWithImage(uiState.player.name, uiState.player.photo)
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.info),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    TeamInfoCard(uiState.player, countryLogo, team)
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.details),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    PlayerDetailsInfoCard(player = uiState.player)
                }
                is PlayerDetailsUiState.NoData -> {
                    EmptyState(
                        modifier = Modifier.fillMaxWidth(),
                        textId = R.string.no_team_details
                    )
                }
            }
        }
    }
    ErrorSnackbar(
        uiState = uiState,
        onErrorClear = onErrorClear,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: DestinationsNavigator) {
    CenterAppTopBar(
        modifier = Modifier
            .height(42.dp)
            .padding(vertical = 8.dp),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = { navigator.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        title = {}
    )
}

@Composable
fun TeamInfoCard(player: Player, countryLogo: String, team: Team) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SimpleInfoRowWithImage(
                    labelId = R.string.country,
                    title = player.nationality,
                    image = countryLogo
                )
                Divider(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    thickness = 2.dp,
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                )
                SimpleInfoRowWithImage(
                    labelId = R.string.team,
                    title = team.name,
                    image = team.logo
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.first_name,
                    title = player.firstname,
                    Icons.Default.Badge
                )
                Divider(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    thickness = 2.dp,
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                )
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.last_name,
                    title = player.lastname,
                    Icons.Default.Badge
                )
            }
        }
    }
}

@Composable
fun PlayerDetailsInfoCard(player: Player) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.age,
                    title = player.age.toString(),
                    Icons.Default.TextIncrease
                )
                Divider(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    thickness = 2.dp,
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                )
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.injured,
                    title = player.injured.toString(),
                    Icons.Default.Flag
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.height,
                    title = player.height,
                    Icons.Default.Height
                )
                Divider(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    thickness = 2.dp,
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                )
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.weight,
                    title = player.weight,
                    Icons.Default.MonitorWeight
                )
            }
        }
    }
}

@Composable
private fun ErrorSnackbar(
    uiState: PlayerDetailsUiState,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when (uiState.error) {
        is PlayerDetailsError.NoError -> {}
        is PlayerDetailsError.EmptyPlayer -> {
            val errorMessageText = stringResource(id = R.string.empty_player_details)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText) {
                snackbarHostState.showSnackbar(message = errorMessageText)
                onErrorDismissState()
            }
        }
    }
}