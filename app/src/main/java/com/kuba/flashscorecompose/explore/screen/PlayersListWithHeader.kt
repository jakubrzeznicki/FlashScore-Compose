package com.kuba.flashscorecompose.explore.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerCountry
import com.kuba.flashscorecompose.ui.component.PlayerCard

/**
 * Created by jrzeznicki on 03/02/2023.
 */
@Composable
fun PlayersListWithHeader(
    players: List<PlayerCountry>,
    state: LazyListState,
    color: Color,
    textId: Int,
    onPlayerClick: (PlayerCountry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state
    ) {
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = textId),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = color,
            )
        }
        items(items = players) {
            PlayerCard(playerCountry = it, onPlayerClick = onPlayerClick)
        }
    }
}

@Composable
fun PlayersDoubleListWithHeader(
    players: List<PlayerCountry>,
    favoritePlayers: List<PlayerCountry>,
    state: LazyListState,
    color: Color,
    favoriteColor: Color,
    textId: Int,
    favoriteTextId: Int,
    onPlayerClick: (PlayerCountry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state
    ) {
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = favoriteTextId),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = favoriteColor,
            )
        }
        items(items = favoritePlayers) {
            PlayerCard(playerCountry = it, onPlayerClick = onPlayerClick)
        }
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp),
                text = stringResource(id = textId),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = color,
            )
        }
        items(items = players) {
            PlayerCard(playerCountry = it, onPlayerClick = onPlayerClick)
        }
    }
}
