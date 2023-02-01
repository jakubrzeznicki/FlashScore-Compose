@file:OptIn(ExperimentalMaterial3Api::class)

package com.kuba.flashscorecompose.countries.screen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.countries.model.CountriesUiState
import com.kuba.flashscorecompose.countries.viewmodel.CountriesViewModel
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.destinations.LeaguesListScreenDestination
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 9/9/2022
 */
private const val SETUP_COUNTRIES_KEY = "SETUP_COUNTRIES_KEY"

@Destination
@Composable
fun CountryListScreen(
    navigator: DestinationsNavigator,
    viewModel: CountriesViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = SETUP_COUNTRIES_KEY) { viewModel.setup() }
    CountriesScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refreshCountries() },
        navigator = navigator,
        onErrorClear = { viewModel.cleanError() },
        context = context
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesScreen(
    modifier: Modifier = Modifier,
    uiState: CountriesUiState,
    onRefreshClick: () -> Unit,
    navigator: DestinationsNavigator,
    onErrorClear: () -> Unit,
    context: Context
) {
    Scaffold(
        topBar = { TopBar(context, navigator) },
    ) { paddingValues ->
        LoadingContent(
            modifier = modifier.padding(paddingValues),
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
                    navigator = navigator,
                    modifier = modifier
                )
                is CountriesUiState.NoCountries -> EmptyScreen(onRefreshClick)
            }
        }
    }
}

@Composable
fun CountryList(
    countries: List<Country>,
    navigator: DestinationsNavigator,
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
                    CountryCard(countryItem = it, navigator = navigator)
                }
            }
        }
    }
}

@Composable
fun CountryCard(countryItem: Country, navigator: DestinationsNavigator) {
    Card(
        onClick = { navigator.navigate(LeaguesListScreenDestination(countryCode = countryItem.code)) },
        elevation = CardDefaults.cardElevation(pressedElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.padding(PaddingValues(vertical = 2.dp))
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
                    .decoderFactory(SvgDecoder.Factory())
                    .data(countryItem.flag)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = countryItem.name,
                style = FlashScoreTypography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun TopBar(context: Context, navigator: DestinationsNavigator) {
    AppTopBar(
        title = {
            Text(
                text = stringResource(id = R.string.countries),
                style = FlashScoreTypography.headlineSmall
            )
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp),
                onClick = { }) {
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