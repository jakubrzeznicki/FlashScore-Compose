package com.kuba.flashscorecompose.fixturedetails.lineup.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Player
import com.kuba.flashscorecompose.fixturedetails.lineup.model.LineupUiState
import com.kuba.flashscorecompose.fixturedetails.lineup.model.TeamTab
import com.kuba.flashscorecompose.fixturedetails.lineup.viewmodel.LineupViewModel
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FilterTextButtonTab
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent
import com.kuba.flashscorecompose.ui.theme.*
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
    LineupList(uiState, { viewModel.refresh() }, { })
}

@Composable
fun LineupList(
    uiState: LineupUiState,
    onRefreshClick: () -> Unit,
    onPlayerClick: (Player) -> Unit
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
                .background(MaterialTheme.colors.background)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is LineupUiState.HasData -> LineupsContent(
                    uiState.homeLineup,
                    uiState.awayLineup,
                    onPlayerClick
                )
                else -> EmptyState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    iconId = R.drawable.ic_close,
                    contentDescriptionId = R.string.load_data_from_network,
                    textId = R.string.no_lineups,
                    onRefreshClick = onRefreshClick
                )
            }
        }
    }
}

@Composable
fun LineupsContent(homeLineup: Lineup, awayLineup: Lineup, onPlayerClick: (Player) -> Unit) {
    val whichTeamSelected = remember { mutableStateOf<TeamTab>(TeamTab.Home) }
    when (whichTeamSelected.value) {
        is TeamTab.Home -> {
            FormationInfoRow(homeLineup.formation)
            Spacer(modifier = Modifier.size(16.dp))
            TeamFormationButtons(
                homeLineup.team.name,
                awayLineup.team.name,
                whichTeamSelected.value
            ) {
                whichTeamSelected.value = TeamTab.Away
            }
            Spacer(modifier = Modifier.size(16.dp))
            FormationImage(homeLineup, onPlayerClick)
        }
        is TeamTab.Away -> {
            FormationInfoRow(awayLineup.formation)
            Spacer(modifier = Modifier.size(16.dp))
            TeamFormationButtons(
                homeLineup.team.name,
                awayLineup.team.name,
                whichTeamSelected.value
            ) {
                whichTeamSelected.value = TeamTab.Home
            }
            Spacer(modifier = Modifier.size(16.dp))
            FormationImage(awayLineup, onPlayerClick)
        }
    }
}

@Composable
fun FormationInfoRow(formation: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(vertical = 8.dp, horizontal = 32.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.formation),
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "($formation)",
            fontSize = 14.sp,
            color = TextGreyLight,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TeamFormationButtons(
    homeTeamName: String,
    awayTeamName: String,
    teamTab: TeamTab,
    onTabChanged: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        FilterTextButtonTab(text = homeTeamName, teamTab is TeamTab.Home, onTabChanged)
        FilterTextButtonTab(text = awayTeamName, teamTab is TeamTab.Away, onTabChanged)
    }
}

@Composable
fun FormationImage(lineup: Lineup, onPlayerClick: (Player) -> Unit) {
    val playersMap = lineup.startXI.groupBy { player ->
        if (player.grid.isNotBlank()) player.grid.firstOrNull().toString().toInt() else 0
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Image(
            painter = painterResource(id = R.drawable.football_field),
            contentDescription = "",
            modifier = Modifier
                .drawWithContent {
                    clipRect(bottom = size.height / 1.2F) {
                        this@drawWithContent.drawContent()
                    }
                }
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )
        PlayersGrid(onPlayerClick, playersMap)
    }
}

@Composable
fun PlayerLineUpItem(player: Player, onPlayerClick: (Player) -> Unit) {
    Column(
        Modifier.clickable { },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(shape = RoundedCornerShape(24.dp), color = GreenLight)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.3f)
                )
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.number.toString(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.White,
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
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PlayersGrid(onPlayerClick: (Player) -> Unit, playersMap: Map<Int, List<Player>>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        playersMap.keys.forEach {
            val formationLine = playersMap.getValue(it)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                formationLine.forEach { player ->
                    PlayerLineUpItem(player = player, onPlayerClick)
                }
            }
        }
    }
}