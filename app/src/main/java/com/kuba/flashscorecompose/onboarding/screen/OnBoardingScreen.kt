package com.kuba.flashscorecompose.onboarding.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.destinations.HomeScreenRouteDestination
import com.kuba.flashscorecompose.onboarding.model.OnBoardingQuestion
import com.kuba.flashscorecompose.onboarding.model.OnBoardingUiState
import com.kuba.flashscorecompose.onboarding.viewmodel.OnBoardingViewModel
import com.kuba.flashscorecompose.ui.component.CenterAppTopBar
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 07/02/2023.
 */
private const val SETUP_ON_BOARDING_KEY = "SETUP_ON_BOARDING_KEY"

@Destination
@Composable
fun OnBoardingRpute(
    navigator: DestinationsNavigator,
    viewModel: OnBoardingViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val teamsScrollState = rememberLazyGridState()
    val playersScrollState = rememberLazyGridState()
    LaunchedEffect(key1 = SETUP_ON_BOARDING_KEY) { viewModel.setup() }
    OnBoardingScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onTeamClick = { viewModel.teamClicked(it) },
        onPlayerClick = { viewModel.playerClicked(it) },
        onClosePressed = {
            viewModel.setOnBoardingAsCompleted()
            navigator.navigate(HomeScreenRouteDestination())
        },
        onNextPressed = { viewModel.onNextPressed() },
        onPreviousPressed = { viewModel.onPreviousPressed() },
        onDonePressed = {
            viewModel.setOnBoardingAsCompleted()
            navigator.navigate(HomeScreenRouteDestination())
        },
        teamsScrollState = teamsScrollState,
        playersScrollState = playersScrollState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    uiState: OnBoardingUiState,
    onRefreshClick: () -> Unit,
    onTeamClick: (Team) -> Unit,
    onPlayerClick: (Player) -> Unit,
    onClosePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
    teamsScrollState: LazyGridState,
    playersScrollState: LazyGridState
) {
    Scaffold(
        topBar = {
            AppTopBar(
                questionIndex = uiState.onBoardingQuestionsData.questionIndex,
                totalQuestionsCount = uiState.onBoardingQuestionsData.questionCount,
                onClosePressed = onClosePressed
            )
        }
    ) { paddingValues ->
        LoadingContent(
            modifier = Modifier.padding(paddingValues),
            empty = when (uiState) {
                is OnBoardingUiState.HasAllData -> false
                is OnBoardingUiState.NoData -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp)
            ) {
                OnBoardingTitle(titleId = uiState.onBoardingQuestionsData.onBoardingQuestion.titleId)
                when (uiState) {
                    is OnBoardingUiState.HasAllData -> {
                        when (uiState.onBoardingQuestionsData.onBoardingQuestion) {
                            is OnBoardingQuestion.Teams -> {
                                TeamsGridList(
                                    teams = uiState.teams,
                                    selectedTeams = uiState.selectedTeams,
                                    teamsScrollState = teamsScrollState,
                                    onTeamClick = onTeamClick
                                )
                            }
                            is OnBoardingQuestion.Players -> {
                                PlayersGridList(
                                    players = uiState.players,
                                    selectedPlayers = uiState.selectedPlayers,
                                    playersScrollState = playersScrollState,
                                    onPlayerClick = onPlayerClick
                                )
                            }
                        }
                    }
                    is OnBoardingUiState.NoData -> {
                        EmptyState(
                            modifier = Modifier.fillMaxWidth(),
                            textId = when (uiState.onBoardingQuestionsData.onBoardingQuestion) {
                                is OnBoardingQuestion.Teams -> R.string.no_teams
                                is OnBoardingQuestion.Players -> R.string.no_players
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(8.dp),
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        }
                    }
                }
                NavigationButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
                    shouldShowPreviousButton = uiState.onBoardingQuestionsData.shouldShowPreviousButton,
                    shouldShowDoneButton = uiState.onBoardingQuestionsData.shouldShowDoneButton,
                    isNextButtonEnabled = uiState.onBoardingQuestionsData.isNextEnabledButton,
                    onPreviousPressed = onPreviousPressed,
                    onNextPressed = onNextPressed,
                    onDonePressed = onDonePressed
                )
            }
        }
    }
}

@Composable
private fun OnBoardingTitle(titleId: Int) {
    Text(
        text = stringResource(
            id = R.string.what_favorite,
            stringResource(id = titleId)
        ),
        color = MaterialTheme.colorScheme.onSecondary,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp
    )
    Spacer(modifier = Modifier.size(16.dp))
    Text(
        text = stringResource(id = R.string.on_boarding_subtitle),
        color = MaterialTheme.colorScheme.inverseOnSurface,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    Spacer(modifier = Modifier.size(32.dp))
}

@Composable
private fun TeamsGridList(
    teams: List<Team>,
    selectedTeams: List<Team>,
    teamsScrollState: LazyGridState,
    onTeamClick: (Team) -> Unit
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        state = teamsScrollState,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        items(items = teams) {
            TeamCard(
                team = it,
                onTeamClick = onTeamClick,
                isSelected = selectedTeams.contains(it)
            )
        }
    }
}

@Composable
private fun PlayersGridList(
    players: List<Player>,
    selectedPlayers: List<Player>,
    playersScrollState: LazyGridState,
    onPlayerClick: (Player) -> Unit
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        state = playersScrollState,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        items(items = players) {
            PlayerCard(
                player = it,
                onPlayerClick = onPlayerClick,
                isSelected = selectedPlayers.contains(it)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    questionIndex: Int,
    totalQuestionsCount: Int,
    onClosePressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CenterAppTopBar(
            title = {
                TopAppBarTitle(
                    questionIndex = questionIndex,
                    totalQuestionsCount = totalQuestionsCount
                )
            },
            actions = {
                IconButton(
                    onClick = onClosePressed,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                    )
                }
            }
        )
        val animatedProgress by animateFloatAsState(
            targetValue = (questionIndex + 1) / totalQuestionsCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}

@Composable
private fun TopAppBarTitle(
    questionIndex: Int,
    totalQuestionsCount: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = (questionIndex + 1).toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = stringResource(R.string.question_count, totalQuestionsCount),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
}

@Composable
private fun TeamCard(
    team: Team = Team.EMPTY_TEAM,
    onTeamClick: (Team) -> Unit = {},
    isSelected: Boolean = true
) {
    val color =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    Column(
        modifier = Modifier
            .padding(4.dp)
            .width(70.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(shape = CircleShape, color = color)
                .border(
                    width = 2.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                .size(70.dp)
                .clickable { onTeamClick(team) },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center)
                    .padding(8.dp),
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(team.logo)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = team.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PlayerCard(
    player: Player = Player.EMPTY_PLAYER,
    onPlayerClick: (Player) -> Unit = {},
    isSelected: Boolean = true
) {
    val color =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    Column(
        Modifier
            .padding(4.dp)
            .width(70.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(shape = CircleShape, color = color)
                .border(
                    width = 2.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                .size(70.dp)
                .clickable { onPlayerClick(player) },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(player.photo)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = player.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NavigationButtons(
    modifier: Modifier,
    shouldShowPreviousButton: Boolean,
    shouldShowDoneButton: Boolean,
    isNextButtonEnabled: Boolean,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            if (shouldShowPreviousButton) {
                Button(
                    onClick = { onPreviousPressed() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = stringResource(id = R.string.previous)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (shouldShowDoneButton) {
                Button(
                    onClick = { onDonePressed() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = isNextButtonEnabled
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = stringResource(id = R.string.done)
                    )
                }
            } else {
                Button(
                    onClick = { onNextPressed() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = isNextButtonEnabled
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = stringResource(id = R.string.next)
                    )
                }
            }
        }
        Button(
            onClick = { onDonePressed() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = stringResource(id = R.string.skip)
            )
        }
    }
}

@Composable
@Preview
private fun PreviewTeamCard() {
    FlashScoreComposeTheme {
        TeamCard()
    }
}
