package com.kuba.flashscorecompose.leagues.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.countries.screen.*
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.leagues.model.DayItem
import com.kuba.flashscorecompose.leagues.model.LeaguesUiState
import com.kuba.flashscorecompose.leagues.viewmodel.LeaguesViewModel
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
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
    countryCode: String,
    navigator: DestinationsNavigator,
    viewModel: LeaguesViewModel = getViewModel(parameters = { parametersOf(countryCode) })
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = SETUP_LEAGUES_KEY) { viewModel.setup() }
    LeaguesScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refreshLeagues() },
        onLeagueClick = { },
        onDayClick = { },
        onErrorClear = { viewModel.cleanError() },
        context = context
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaguesScreen(
    modifier: Modifier = Modifier,
    uiState: LeaguesUiState,
    onRefreshClick: () -> Unit,
    onLeagueClick: (League) -> Unit,
    onDayClick: (DayItem) -> Unit,
    onErrorClear: () -> Unit,
    context: Context
) {
    Scaffold(topBar = { TopBar(context) }) { paddingValues ->
        LoadingContent(
            modifier = Modifier.padding(paddingValues),
            empty = when (uiState) {
                is LeaguesUiState.HasLeagues -> false
                is LeaguesUiState.NoLeagues -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DaysList(days = uiState.dayItems, onDayClick = onDayClick, modifier = modifier)
                Spacer(modifier = Modifier.height(4.dp))
                when (uiState) {
                    is LeaguesUiState.HasLeagues -> LeaguesList(
                        leagues = uiState.leagueItems,
                        favoritesLeagues = uiState.favoriteLeagueItems,
                        onLeagueClick = onLeagueClick,
                        modifier = modifier
                    )
                    is LeaguesUiState.NoLeagues -> EmptyScreen(onRefreshClick)
                }
            }
        }
    }
}

@Composable
fun DaysList(
    days: List<DayItem>,
    onDayClick: (DayItem) -> Unit,
    modifier: Modifier,
    state: LazyListState = rememberLazyListState()
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val selectedIndex = remember { mutableStateOf(6) }
//    coroutineScope.launch { listState.scrollToItem(selectedIndex.value) }
//    LazyRow(modifier = modifier, state = listState) {
//        itemsIndexed(items = days) { index, item ->
//            coroutineScope.launch { listState.scrollToItem(selectedIndex.value) }
//            Row {
//                DayCard(
//                    dayItem = item,
//                    onDayClick = onDayClick,
//                    selectedIndex = selectedIndex,
//                    state
//                )
//            }
//        }
//        CoroutineScope(Dispatchers.Main).launch {
//            listState.scrollToItem(selectedIndex.value)
//        }
//    }
}

@Composable
fun DayCard(
    dayItem: DayItem,
    onDayClick: (DayItem) -> Unit,
    selectedIndex: MutableState<Int>,
    state: LazyListState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                selectedIndex.value = dayItem.index
                onDayClick(dayItem)
            },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color = if (selectedIndex.value == dayItem.index) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.secondary
        }
        Text(text = dayItem.weekDay, color = color)
        Text(text = dayItem.formattedDate, color = color)
    }
}

@Composable
fun LeaguesList(
    leagues: List<League>,
    favoritesLeagues: List<League>,
    onLeagueClick: (League) -> Unit,
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    val favoritesListState = rememberLazyListState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        if (favoritesLeagues.isNotEmpty()) {
            FavoritesSection(
                contentPadding = contentPadding,
                state = favoritesListState,
                favoritesLeagues = favoritesLeagues,
                onLeagueClick = onLeagueClick
            )
        }
        Text(
            text = "Others competitions [A-Z]".uppercase(),
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.W500
        )
        LazyColumn(modifier = modifier, contentPadding = contentPadding, state = state) {
            item {
                Column {
                    leagues.forEach {
                        LeagueItem(leagueItem = it, onLeagueClick = onLeagueClick)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesSection(
    contentPadding: PaddingValues,
    state: LazyListState,
    favoritesLeagues: List<League>,
    onLeagueClick: (League) -> Unit
) {
    Text(
        text = "Favorite competitions".uppercase(),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.W500
    )
    LazyColumn(contentPadding = contentPadding, state = state) {
        item {
            Column {
                favoritesLeagues.forEach {
                    LeagueItem(leagueItem = it, onLeagueClick = onLeagueClick)
                }
            }
        }
    }
}

@Composable
fun LeagueItem(leagueItem: League, onLeagueClick: (League) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data(leagueItem.logo)
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = leagueItem.name.uppercase(), color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(context: Context) {
    AppTopBar(
        modifier = Modifier
            .height(58.dp)
            .padding(vertical = 8.dp),
        title = {
            Text(
                text = stringResource(id = R.string.leagues),
                style = FlashScoreTypography.headlineSmall
            )
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp),
                onClick = { Toast.makeText(context, "settings", Toast.LENGTH_SHORT).show() }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "settings")
            }
        }

    )
}

@Composable
fun EmptyScreen(onRefreshClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            modifier = Modifier.size(128.dp),
            contentDescription = ""
        )
        Text(text = "No countries", modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = onRefreshClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text(
                text = "Refresh",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}