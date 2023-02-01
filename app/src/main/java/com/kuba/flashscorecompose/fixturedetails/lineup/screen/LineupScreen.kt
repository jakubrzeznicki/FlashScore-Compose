package com.kuba.flashscorecompose.fixturedetails.lineup.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.fixturedetails.lineup.model.LineupUiState
import com.kuba.flashscorecompose.fixturedetails.lineup.viewmodel.LineupViewModel
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent
import com.kuba.flashscorecompose.ui.theme.GreenDark
import com.kuba.flashscorecompose.ui.theme.GreenLight
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 23/12/2022.
 */
private const val LINEUP_KEYS = "LINEUP_KEY"

@Composable
fun LineupScreen(
    fixtureId: Int,
    navigator: DestinationsNavigator,
    viewModel: LineupViewModel = getViewModel { parametersOf(fixtureId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = LINEUP_KEYS) { viewModel.setup() }
    LineupList(
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onPlayerClick = { },
        onLineupClick = { viewModel.changeSelectedLineup(it) }
    )
}

@Composable
private fun LineupList(
    uiState: LineupUiState,
    onRefreshClick: () -> Unit,
    onPlayerClick: (Player) -> Unit,
    onLineupClick: (Lineup) -> Unit
) {
    val scrollState = rememberScrollState()
    LoadingContent(
        empty = when (uiState) {
            is LineupUiState.HasData -> false
            else -> uiState.isLoading
        }, emptyContent = { FullScreenLoading() },
        loading = uiState.isLoading,
        onRefresh = onRefreshClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is LineupUiState.HasData -> {
                    FormationInfoRow(uiState.selectedLineup.formation)
                    LineupButtons(
                        lineups = uiState.lineups,
                        selectedLineup = uiState.selectedLineup,
                        onLineupClick = onLineupClick
                    )
                    LineupFieldImage(uiState.selectedLineup, onPlayerClick)
                }
                else -> EmptyState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    textId = R.string.no_lineups
                )
            }
        }
    }
}

@Composable
private fun FormationInfoRow(formation: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = stringResource(id = R.string.formation),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "($formation)",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LineupButtons(
    lineups: List<Lineup>,
    selectedLineup: Lineup,
    onLineupClick: (Lineup) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        LineupChip(
            lineup = lineups.first(),
            isSelected = lineups.first().team.id == selectedLineup.team.id,
            onChipClick = onLineupClick
        )
        LineupChip(
            lineup = lineups.last(),
            isSelected = lineups.last().team.id == selectedLineup.team.id,
            onChipClick = onLineupClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LineupChip(
    lineup: Lineup,
    isSelected: Boolean,
    onChipClick: (Lineup) -> Unit
) {
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            iconColor = MaterialTheme.colorScheme.onSecondary,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.inverseOnSurface,
            selectedLeadingIconColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        label = {
            Text(
                text = lineup.team.name,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        },
        shape = RoundedCornerShape(50),
        leadingIcon = {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(18.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(lineup.team.logo)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        },
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.inverseSurface
        ),
        onClick = { onChipClick(lineup) }
    )
}

@Composable
fun LineupFieldImage(lineup: Lineup, onPlayerClick: (Player) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(32.dp))
            .padding(top = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.football_field),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        PlayersGrid(onPlayerClick, lineup.startXIWithPosition)
    }
}

@Composable
fun PlayersGrid(onPlayerClick: (Player) -> Unit, playersWithPosition: Map<Int, List<Player>>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        playersWithPosition.keys.forEach {
            val formationLine = playersWithPosition.getValue(it)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                formationLine.forEach { player ->
                    PlayerItem(player = player, onPlayerClick)
                }
            }
        }
    }
}

@Composable
fun PlayerItem(player: Player, onPlayerClick: (Player) -> Unit) {
    Column(
        Modifier.clickable { onPlayerClick(player) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(shape = RoundedCornerShape(24.dp), color = GreenLight)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f)
                )
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.number.toString(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(color = GreenDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.name.take(14),
                modifier = Modifier.padding(3.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}