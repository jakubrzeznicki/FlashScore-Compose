package com.example.explore.screen

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
import com.example.model.player.PlayerWrapper
import com.example.ui.composables.PlayerCard

/**
 * Created by jrzeznicki on 03/02/2023.
 */
@Composable
fun PlayersListWithHeader(
    players: List<PlayerWrapper>,
    state: LazyListState,
    color: Color,
    textId: Int,
    onPlayerClick: (PlayerWrapper) -> Unit,
    onPlayerFavoriteClick: (PlayerWrapper) -> Unit
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
                color = color
            )
        }
        items(items = players) {
            PlayerCard(
                playerWrapper = it,
                onPlayerClick = onPlayerClick,
                onFavoriteClick = onPlayerFavoriteClick
            )
        }
    }
}

@Composable
fun PlayersDoubleListWithHeader(
    players: List<PlayerWrapper>,
    favoritePlayers: List<PlayerWrapper>,
    state: LazyListState,
    color: Color,
    favoriteColor: Color,
    textId: Int,
    favoriteTextId: Int,
    onPlayerClick: (PlayerWrapper) -> Unit,
    onPlayerFavoriteClick: (PlayerWrapper) -> Unit
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
                color = favoriteColor
            )
        }
        items(items = favoritePlayers) {
            PlayerCard(
                playerWrapper = it,
                onPlayerClick = onPlayerClick,
                onFavoriteClick = onPlayerFavoriteClick
            )
        }
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp),
                text = stringResource(id = textId),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = color
            )
        }
        items(items = players) {
            PlayerCard(
                playerWrapper = it,
                onPlayerClick = onPlayerClick,
                onFavoriteClick = onPlayerFavoriteClick
            )
        }
    }
}
