package com.kuba.flashscorecompose.explore.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.ui.component.VenueCard

/**
 * Created by jrzeznicki on 03/02/2023.
 */
@Composable
fun VenuesList(
    venues: List<Venue>,
    state: LazyGridState,
    onVenueClick: (Venue) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = state,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = venues) {
            VenueCard(venue = it, onVenueClick = onVenueClick)
        }
    }
}
