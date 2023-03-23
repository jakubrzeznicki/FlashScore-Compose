package com.example.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex

/**
 * Created by jrzeznicki on 19/01/2023.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
    pullRefreshState: PullRefreshState,
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        Box(
            modifier = modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
                .zIndex(-1f)
        ) {
            content()
            PullRefreshIndicator(
                refreshing = loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}
