package com.kuba.flashscorecompose.fixturedetails.lineup.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.relay.compose.BoxScopeInstanceImpl.matchParentSize
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.countries.screen.FullScreenLoading
import com.kuba.flashscorecompose.countries.screen.LoadingContent
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Player
import com.kuba.flashscorecompose.fixturedetails.lineup.model.LineupUiState
import com.kuba.flashscorecompose.fixturedetails.lineup.viewmodel.LineupViewModel
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsUiState
import com.kuba.flashscorecompose.ui.theme.*
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 23/12/2022.
 */
private const val LINEUP_KEYS = "LINEUP_KEY"

@Composable
fun LineupScreen(
    fixtureId: Int,
    viewModel: LineupViewModel = getViewModel { parametersOf(fixtureId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = LINEUP_KEYS) { viewModel.setup() }
    LineupList(uiState) { viewModel.refresh() }
}

@Composable
fun LineupList(uiState: LineupUiState, onRefreshClick: () -> Unit) {
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
                is LineupUiState.HasData -> LineupsContent(uiState.lineups)
                else -> EmptyLineups()
            }
        }
    }
}

@Composable
fun LineupsContent(lineups: List<Lineup>) {
    val whichTeamSelected = remember { mutableStateOf<TeamTab>(TeamTab.Home) }
    FormationInfoRow(
        formation = when (whichTeamSelected.value) {
            is TeamTab.Home -> lineups.firstOrNull()?.formation.orEmpty()
            is TeamTab.Away -> lineups.lastOrNull()?.formation.orEmpty()
        }
    )
    Spacer(modifier = Modifier.size(16.dp))
    val teamNames = lineups.map { it.team.name }
    TeamFormationButtons(teamNames, whichTeamSelected.value) {
        when (whichTeamSelected.value) {
            is TeamTab.Home -> whichTeamSelected.value = TeamTab.Away
            is TeamTab.Away -> whichTeamSelected.value = TeamTab.Home
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
    when (whichTeamSelected.value) {
        is TeamTab.Home -> FormationImage(lineups.first())
        is TeamTab.Away -> FormationImage(lineups.last())
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
            text = "Formation",
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
fun TeamFormationButtons(names: List<String>, teamTab: TeamTab, onTabChanged: () -> Unit) {
    val isHomeActive = when (teamTab) {
        is TeamTab.Home -> true
        is TeamTab.Away -> false
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        TextButtonTab(text = names.firstOrNull().orEmpty(), isHomeActive, onTabChanged)
        TextButtonTab(text = names.lastOrNull().orEmpty(), !isHomeActive, onTabChanged)
    }
}

@Composable
fun TextButtonTab(text: String, isActive: Boolean, onTabChanged: () -> Unit) {
    TextButton(
        onClick = onTabChanged,
        modifier = if (isActive) {
            Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(colors = listOf(LightOrange, Orange))
                )
                .padding(horizontal = 16.dp)
        } else {
            Modifier
                .clip(RoundedCornerShape(50))
        },
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FormationImage(lineup: Lineup) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Image(
            painter = painterResource(id = R.drawable.football_pitch),
            contentDescription = "",
            modifier = Modifier
                .drawWithContent {
                    clipRect(bottom = size.height / 1.6f) {
                        this@drawWithContent.drawContent()
                    }
                }
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )
        PlayersGrid(players = lineup.startXI, formation = lineup.formation)
    }
}

@Composable
fun PlayerLineUpItem(player: Player) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                text = player.name,
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.White,
            )
        }
    }
}

@Composable
fun PlayersGrid(players: List<Player>, formation: String) {
    val gridMap = players.groupBy { player ->
        if (player.grid.isNotBlank()) player.grid.firstOrNull().toString().toInt() else 0
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        gridMap.keys.forEach {
            val formationLine = gridMap.getValue(it)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                formationLine.forEach { player ->
                    PlayerLineUpItem(player = player)
                }
            }
        }
    }
}

@Composable
fun EmptyLineups() {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            modifier = Modifier.size(40.dp),
            contentDescription = ""
        )
        Text("No Lineups", modifier = Modifier.padding(16.dp))
    }
}

sealed interface TeamTab {
    object Home : TeamTab
    object Away : TeamTab
}