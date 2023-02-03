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
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.explore.model.CoachCountry
import com.kuba.flashscorecompose.ui.component.CoachCard
import com.kuba.flashscorecompose.ui.component.FixtureCard

/**
 * Created by jrzeznicki on 03/02/2023.
 */
@Composable
fun CoachesListWithHeader(
    coaches: List<CoachCountry>,
    state: LazyListState,
    color: Color,
    textId: Int,
    onCoachClick: (CoachCountry) -> Unit
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
        items(items = coaches) {
            CoachCard(coachCountry = it, onCoachClick = onCoachClick)
        }
    }
}