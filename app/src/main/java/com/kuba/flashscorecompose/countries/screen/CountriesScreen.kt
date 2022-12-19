package com.kuba.flashscorecompose.countries.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.countries.model.CountriesUiState
import com.kuba.flashscorecompose.countries.viewmodel.CountriesViewModel
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.destinations.LeaguesListScreenDestination
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 9/9/2022
 */
private const val SETUP_COUNTRIES_KEY = "SETUP_COUNTRIES_KEY"

@Destination(start = true)
@Composable
fun CountryListScreen(
    navigator: DestinationsNavigator,
    viewModel: CountriesViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = SETUP_COUNTRIES_KEY) { viewModel.setup() }
    CountriesScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refreshCountries() },
        onCountryClick = { navigator.navigate(LeaguesListScreenDestination(countryId = "countryId")) },
        onErrorClear = { viewModel.cleanError() },
        scaffoldState = scaffoldState,
        context = context
    )
}

@Composable
fun CountriesScreen(
    modifier: Modifier = Modifier,
    uiState: CountriesUiState,
    onRefreshClick: () -> Unit,
    onCountryClick: (Country) -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState,
    context: Context
) {
    Scaffold(topBar = { TopBar(context) }, scaffoldState = scaffoldState) {
        LoadingContent(
            empty = when (uiState) {
                is CountriesUiState.HasCountries -> false
                is CountriesUiState.NoCountries -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            when (uiState) {
                is CountriesUiState.HasCountries -> CountryList(
                    countries = uiState.countryItems,
                    onCountryClick = onCountryClick,
                    modifier = modifier
                )
                is CountriesUiState.NoCountries -> EmptyScreen(onRefreshClick)
            }
        }
    }
}

@Composable
fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = loading),
            onRefresh = onRefresh,
            content = content
        )
    }
}

@Composable
fun CountryList(
    countries: List<Country>,
    onCountryClick: (Country) -> Unit,
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        state = state
    ) {
        item {
            Column {
                countries.forEach {
                    CountryCard(countryItem = it, onCountryClick = onCountryClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CountryCard(countryItem: Country, onCountryClick: (Country) -> Unit) {
    Card(
        onClick = { onCountryClick(countryItem) },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(countryItem.logo)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = countryItem.name.orEmpty(), style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun TopBar(context: Context) {
    AppTopBar(
        shape = RoundedCornerShape(0, 0, 24, 24),
        title = {
            Text(text = stringResource(id = R.string.countries))
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp),
                onClick = { Toast.makeText(context, "settings", Toast.LENGTH_SHORT) }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "settings")
            }
        }
    )
}

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
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
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(
                text = "Refresh",
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}