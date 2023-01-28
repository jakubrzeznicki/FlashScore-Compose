package com.kuba.flashscorecompose.ui.component.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 27/01/2023.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(
    tabs: List<TabItem>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(1.dp),
        contentColor = MaterialTheme.colorScheme.onSecondary,
        containerColor = MaterialTheme.colorScheme.background,
        indicator = { Box {} },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            DetailsTab(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                tab = tab,
                index = index
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScrollableTabs(
    tabs: List<TabItem>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
) {
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(1.dp),
        contentColor = MaterialTheme.colorScheme.onSecondary,
        containerColor = MaterialTheme.colorScheme.background,
        edgePadding = 0.dp,
        indicator = { Box {} },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            DetailsTab(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                tab = tab,
                index = index
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun DetailsTab(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    tab: TabItem,
    index: Int
) {
    Tab(
        modifier = if (pagerState.currentPage == index) {
            Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
        } else {
            Modifier
                .clip(RoundedCornerShape(50))
        },
        text = { Text(stringResource(id = tab.titleId)) },
        selected = pagerState.currentPage == index,
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen()
    }
}