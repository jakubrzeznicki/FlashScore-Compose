package com.kuba.flashscorecompose.teamdetails.informations.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.team.information.model.Coach
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.teamdetails.informations.model.TeamInformationsUiState
import com.kuba.flashscorecompose.teamdetails.informations.viewmodel.TeamInformationsViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 27/01/2023.
 */

private const val TEAM_INFORMATIONS_KEY = "TEAM_INFORMATIONS_KEY"

@Composable
fun TeamInformationsScreen(
    team: Team,
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator,
    viewModel: TeamInformationsViewModel = getViewModel {
        parametersOf(team, leagueId, season)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = TEAM_INFORMATIONS_KEY) { viewModel.setup() }
    InformationsScreen(uiState = uiState, onRefreshClick = { viewModel.refresh() })
}

@Composable
fun InformationsScreen(uiState: TeamInformationsUiState, onRefreshClick: () -> Unit) {
    val scrollState = rememberScrollState()
    LoadingContent(
        empty = when (uiState) {
            is TeamInformationsUiState.HasFullData -> false
            is TeamInformationsUiState.HasDataWithoutCoach -> false
            is TeamInformationsUiState.HasDataWithoutVenueAndCoach -> false
            else -> uiState.isLoading
        },
        emptyContent = { FullScreenLoading() },
        loading = uiState.isLoading,
        onRefresh = onRefreshClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 16.dp)
        ) {
            when (uiState) {
                is TeamInformationsUiState.HasFullData -> {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.info),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    TeamInfoCard(team = uiState.team, uiState.country)
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.venue),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    VenueInfoCard(venue = uiState.venue)
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.coach),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    CoachInfoCard(coach = uiState.coach)
                }
                is TeamInformationsUiState.HasDataWithoutCoach -> {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.info),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    TeamInfoCard(team = uiState.team, uiState.country)
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.venue),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    VenueInfoCard(venue = uiState.venue)
                }
                is TeamInformationsUiState.HasDataWithoutVenueAndCoach -> {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.info),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    TeamInfoCard(team = uiState.team, uiState.country)
                }
                is TeamInformationsUiState.NoData -> {
                    EmptyState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        textId = R.string.no_team_informations
                    )
                }
            }
        }
    }
}

@Composable
fun TeamInfoCard(team: Team, country: Country) {
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
            InfoRowWithImage(
                labelId = R.string.country,
                title = team.country,
                image = country.flag
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.founded,
                    title = team.founded.toString(),
                    Icons.Default.Foundation
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
                    labelId = R.string.is_national,
                    title = team.isNational.toString(),
                    Icons.Default.Flag
                )
            }
        }
    }
}

@Composable
fun VenueInfoCard(venue: Venue) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
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
            InfoRowWithIcon(
                modifier = Modifier.weight(8f),
                labelId = R.string.name,
                title = venue.name,
                Icons.Default.Stadium
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.city,
                    title = venue.city,
                    Icons.Default.LocationCity
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
                    labelId = R.string.address,
                    title = venue.address,
                    Icons.Default.Home
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
                    labelId = R.string.capacity,
                    title = venue.capacity.toString(),
                    Icons.Default.ReduceCapacity
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
                    labelId = R.string.surface,
                    title = venue.surface,
                    Icons.Default.AreaChart
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(venue.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
fun CoachInfoCard(coach: Coach) {
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
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(coach.photo)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoRowWithIcon(
                    modifier = Modifier.weight(8f),
                    labelId = R.string.first_name,
                    title = coach.firstname,
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
                    title = coach.lastname,
                    Icons.Default.Badge
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
                    labelId = R.string.age,
                    title = coach.age.toString(),
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
                    labelId = R.string.nationality,
                    title = coach.nationality,
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
                    title = coach.height,
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
                    title = coach.weight,
                    Icons.Default.MonitorWeight
                )
            }
        }
    }
}