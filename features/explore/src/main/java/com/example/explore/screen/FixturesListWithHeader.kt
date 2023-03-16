package com.example.explore.screen

import android.content.Context
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
import com.example.model.fixture.FixtureItemWrapper
import com.example.ui.composables.FixtureCard

/**
 * Created by jrzeznicki on 03/02/2023.
 */
@Composable
fun FixturesListWithHeader(
    fixtures: List<FixtureItemWrapper>,
    state: LazyListState,
    color: Color,
    textId: Int,
    context: Context,
    onFixtureClick: (FixtureItemWrapper) -> Unit,
    onFavoriteClick: (FixtureItemWrapper) -> Unit
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
        items(items = fixtures) {
            FixtureCard(
                fixtureItemWrapper = it,
                context = context,
                onFixtureClick = onFixtureClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun FixturesDoubleListWithHeader(
    fixtures: List<FixtureItemWrapper>,
    favoriteFixtures: List<FixtureItemWrapper>,
    state: LazyListState,
    color: Color,
    favoriteColor: Color,
    textId: Int,
    favoriteTextId: Int,
    context: Context,
    onFixtureClick: (FixtureItemWrapper) -> Unit,
    onFavoriteClick: (FixtureItemWrapper) -> Unit
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
        items(items = favoriteFixtures) {
            FixtureCard(
                fixtureItemWrapper = it,
                context = context,
                onFixtureClick = onFixtureClick,
                onFavoriteClick = onFavoriteClick
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
        items(items = fixtures) {
            FixtureCard(
                fixtureItemWrapper = it,
                context = context,
                onFixtureClick = onFixtureClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}
