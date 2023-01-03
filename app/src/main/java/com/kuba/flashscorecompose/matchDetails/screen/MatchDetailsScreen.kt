package com.kuba.flashscorecompose.matchDetails.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.relay.compose.BoxScopeInstanceImpl.matchParentSize
import com.kuba.flashscorecompose.home.FixtureData
import com.kuba.flashscorecompose.home.FixtureWidgetCard
import com.kuba.flashscorecompose.home.Team
import com.kuba.flashscorecompose.ui.theme.TextGreyLight
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by jrzeznicki on 23/12/2022.
 */
@Composable
fun MatchDetailsScreen(homeTeamStatistics: Statistics, awayTeamStatistics: Statistics) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .matchParentSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colors.background)
            .padding(PaddingValues(vertical = 16.dp)),
        ) {
        StatisticDetailRow(homeTeamStatistics.shooting, awayTeamStatistics.shooting, "Shooting")
        StatisticDetailRow(homeTeamStatistics.attacks, awayTeamStatistics.attacks, "Attacks")
        StatisticDetailRow(
            homeTeamStatistics.possession,
            awayTeamStatistics.possession,
            "Possession"
        )
        StatisticDetailRow(homeTeamStatistics.cards, awayTeamStatistics.cards, "Cards")
        StatisticDetailRow(homeTeamStatistics.corners, awayTeamStatistics.corners, "Corners")
        Spacer(modifier = Modifier.size(24.dp))
        OtherMatchHeader()
        Spacer(modifier = Modifier.size(16.dp))
        FixtureWidgets(fixtureWidgetsList = provideFixtureDataList())
    }
}

@Composable
fun StatisticDetailRow(homeValue: Int, awayValue: Int, stat: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(vertical = 8.dp, horizontal = 32.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(
            text = homeValue.toString(),
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = stat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = TextGreyLight,
            textAlign = TextAlign.Center
        )
        Text(
            text = awayValue.toString(),
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OtherMatchHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Other Match",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.White,
        )
        Text(
            text = "See all",
            fontSize = 12.sp,
            color = TextGreyLight,
        )
    }
}

fun provideFixtureDataList(): List<FixtureData> {
    return (0..8).map {
        FixtureData(
            homeTeam = Team(
                name = "Burnley$it",
                logo = "https://media.api-sports.io/football/teams/44.png"
            ),
            awayTeam = Team(
                name = "Manchester United$it",
                logo = "https://media.api-sports.io/football/teams/33.png"
            ),
            homeTeamScore = 0 + it,
            awayTeamScore = 2 + it
        )
    }
}

@Composable
fun FixtureWidgets(fixtureWidgetsList: List<FixtureData>) {
    Column {
        fixtureWidgetsList.forEach {
            FixtureWidgetCard(fixtureData = it)
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}