package com.kuba.flashscorecompose.standingsdetails.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.relay.compose.BoxScopeInstanceImpl.matchParentSize
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.standingsdetails.model.StandingFilterButton
import com.kuba.flashscorecompose.standingsdetails.model.StandingsDetailsUiState
import com.kuba.flashscorecompose.standingsdetails.viewmodel.StandingsDetailsViewModel
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 19/01/2023.
 */

private const val SETUP_STANDINGS_DETAILS_KEY = "SETUP_STANDINGS_DETAILS_KEY"
private const val HASZTAG = "#"

@Destination(route = "standings/standingsdetails")
@Composable
fun StandingsDetailsRoute(
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator,
    viewModel: StandingsDetailsViewModel = getViewModel { parametersOf(leagueId, season) },
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_STANDINGS_DETAILS_KEY) { viewModel.setup() }
    StandingsDetailsScreen(
        uiState = uiState,
        navigator = navigator,
        onTeamClick = {},
        scaffoldState = scaffoldState,
        onStandingsFilteredButtonClick = { viewModel.filterStandings(it) }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun StandingsDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: StandingsDetailsUiState,
    navigator: DestinationsNavigator,
    onTeamClick: (Team) -> Unit,
    scaffoldState: ScaffoldState,
    onStandingsFilteredButtonClick: (StandingFilterButton) -> Unit
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(navigator, uiState.league) },
        scaffoldState = scaffoldState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            state = scrollState
        ) {
            item {
                LeagueHeader(uiState.league)
            }
            item {
                StandingFilterChips(uiState.standingFilterButton, onStandingsFilteredButtonClick)
            }
            item {
                StandingHeaderRow()
                Divider(
                    color = GreyDark,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
            items(items = uiState.standingsItems) {
                StandingElementRow(it, onTeamClick)
            }
        }
    }
}

@Composable
private fun TopBar(navigator: DestinationsNavigator, league: League) {
    AppTopBar(
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(24.dp),
                onClick = { navigator.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.matchParentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .decoderFactory(SvgDecoder.Factory())
                        .data(league.countryFlag)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Text(text = league.countryName, color = Color.White)
            }
        })
}

@Composable
private fun LeagueHeader(league: League) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(shape = CircleShape, color = GreyDark)
                .border(width = 2.dp, shape = CircleShape, color = GreyLight)
                .size(112.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(90.dp)
                    .padding(12.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(league.logo)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = league.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StandingFilterChips(
    selectedFilteredButton: StandingFilterButton,
    onFilteredChanged: (StandingFilterButton) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        FilteredTextButton(
            filteredButton = StandingFilterButton.All,
            isSelected = selectedFilteredButton is StandingFilterButton.All,
            onStateChanged = onFilteredChanged
        )
        FilteredTextButton(
            filteredButton = StandingFilterButton.Home,
            isSelected = selectedFilteredButton is StandingFilterButton.Home,
            onStateChanged = onFilteredChanged
        )
        FilteredTextButton(
            filteredButton = StandingFilterButton.Away,
            isSelected = selectedFilteredButton is StandingFilterButton.Away,
            onStateChanged = onFilteredChanged
        )
    }
}

@Composable
fun FilteredTextButton(
    filteredButton: StandingFilterButton,
    isSelected: Boolean,
    onStateChanged: (StandingFilterButton) -> Unit
) {
    TextButton(
        onClick = { onStateChanged(filteredButton) },
        modifier = if (isSelected) {
            Modifier
                .height(50.dp)
                .background(
                    brush = Brush.horizontalGradient(colors = listOf(LightOrange, Orange)),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 4.dp)
        } else {
            Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(50))
                .padding(horizontal = 4.dp)
        }
    ) {
        Text(
            text = stringResource(id = filteredButton.textId),
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StandingHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = HASZTAG,
            fontSize = 14.sp,
            color = TextGreyLight
        )
        Text(
            modifier = Modifier.weight(6f),
            text = stringResource(id = R.string.team),
            fontSize = 14.sp,
            color = TextGreyLight
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.wins),
            fontSize = 14.sp,
            color = TextGreyLight
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.loses),
            fontSize = 14.sp,
            color = TextGreyLight
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.draws),
            fontSize = 14.sp,
            color = TextGreyLight
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.goals_diff),
            fontSize = 14.sp,
            color = TextGreyLight
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.points),
            fontSize = 14.sp,
            color = TextGreyLight
        )
    }
}

@Composable
private fun StandingElementRow(standingItem: StandingItem, onTeamClick: (Team) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onTeamClick(standingItem.team) }
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = colorResource(id = standingItem.colorId),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.rank.toString(),
            fontSize = 14.sp,
            color = White
        )
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .size(28.dp)
                .padding(end = 4.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data(standingItem.team.logo)
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            modifier = Modifier
                .weight(5f)
                .padding(end = 4.dp),
            text = standingItem.team.name.take(16),
            fontSize = 14.sp,
            color = White,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.selectedInformationStanding.win.toString(),
            fontSize = 14.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.selectedInformationStanding.lose.toString(),
            fontSize = 14.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.selectedInformationStanding.draw.toString(),
            fontSize = 14.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.goalsDiff.toString(),
            fontSize = 14.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.points.toString(),
            fontSize = 14.sp,
            color = White
        )
    }
}