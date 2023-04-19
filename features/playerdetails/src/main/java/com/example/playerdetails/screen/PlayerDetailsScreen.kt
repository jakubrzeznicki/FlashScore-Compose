package com.example.playerdetails.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.player.Player
import com.example.model.team.Team
import com.example.playerdetails.R
import com.example.playerdetails.model.PlayerDetailsUiState
import com.example.playerdetails.navigation.PlayerDetailsNavigator
import com.example.playerdetails.viewmodel.PlayerDetailsViewModel
import com.example.ui.composables.*
import com.example.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import com.example.ui.R as uiR

/**
 * Created by jrzeznicki on 01/02/2023.
 */
private const val SETUP_PLAYER_DETAILS_KEY = "SETUP_PLAYER_DETAILS_KEY"

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun PlayerDetailsRoute(
    playerId: Int,
    team: Team,
    season: Int,
    navigator: PlayerDetailsNavigator,
    viewModel: PlayerDetailsViewModel = getViewModel { parametersOf(playerId, team, season) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.refresh() }
    )
    LaunchedEffect(key1 = SETUP_PLAYER_DETAILS_KEY) { viewModel.setup() }
    PlayerDetailsScreen(
        uiState = uiState,
        team = team,
        navigator = navigator,
        pullRefreshState = pullRefreshState,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PlayerDetailsScreen(
    uiState: PlayerDetailsUiState,
    team: Team,
    navigator: PlayerDetailsNavigator,
    pullRefreshState: PullRefreshState,
) {
    Scaffold(
        topBar = {
            TopBar(
                navigator = navigator,
                title = when (uiState) {
                    is PlayerDetailsUiState.HasData -> uiState.player.name
                    else -> ""
                }
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        LoadingContent(
            modifier = Modifier.padding(paddingValues),
            empty = when (uiState) {
                is PlayerDetailsUiState.HasData -> false
                else -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            pullRefreshState = pullRefreshState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(scrollState)
            ) {
                when (uiState) {
                    is PlayerDetailsUiState.HasData -> {
                        HeaderDetails(uiState.player.photo)
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = stringResource(id = uiR.string.info),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        TeamInfoCard(uiState.player, uiState.country?.flag.orEmpty(), team)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = stringResource(id = R.string.details),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        PlayerDetailsInfoCard(player = uiState.player)
                    }
                    is PlayerDetailsUiState.NoData -> {
                        EmptyState(
                            modifier = Modifier.fillMaxWidth(),
                            textId = R.string.no_player_details
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: PlayerDetailsNavigator, title: String) {
    CenterAppTopBar(
        modifier = Modifier
            .height(48.dp)
            .padding(top = 8.dp),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(32.dp),
                onClick = { navigator.navigateUp() }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSecondary,
                style = FlashScoreTypography.headlineSmall
            )
        }
    )
}

@Composable
fun TeamInfoCard(player: Player, countryLogo: String, team: Team) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                    modifier = Modifier.weight(10f),
                    labelId = uiR.string.country,
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
                    modifier = Modifier.weight(10f),
                    labelId = uiR.string.team,
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
                    labelId = uiR.string.first_name,
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
                    labelId = uiR.string.last_name,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                    labelId = uiR.string.age,
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
                    labelId = uiR.string.height,
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
                    labelId = uiR.string.weight,
                    title = player.weight,
                    Icons.Default.MonitorWeight
                )
            }
        }
    }
}
