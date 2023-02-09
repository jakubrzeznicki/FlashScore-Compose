package com.kuba.flashscorecompose.profile.container.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.profile.container.model.ProfileUiState
import com.kuba.flashscorecompose.profile.container.viewmodel.ProfileViewModel
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.component.HeaderDetailsWithImage
import com.kuba.flashscorecompose.ui.component.tabs.ScrollableTabs
import com.kuba.flashscorecompose.ui.component.tabs.TabItem
import com.kuba.flashscorecompose.ui.component.tabs.TabsContent
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 08/02/2023.
 */
private const val SETUP_PROFILE_KEY = "SETUP_PROFILE_KEY"

@Destination
@Composable
fun ProfileRoute(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_PROFILE_KEY) { viewModel.setup() }
    ProfileScreen(
        uiState = uiState,
        onImageClick = {} // change profile image
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(uiState: ProfileUiState, onImageClick: () -> Unit) {
    Scaffold(
        topBar = { TopBar() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
        ) {
            HeaderDetailsWithImage(uiState.user.name, uiState.user.photoUri, onImageClick)
            ProfileTabs(user = uiState.user)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    AppTopBar(
        modifier = Modifier
            .height(48.dp)
            .padding(vertical = 8.dp),
        title = {
            Text(
                text = stringResource(id = R.string.profile),
                color = MaterialTheme.colorScheme.onSecondary,
                style = FlashScoreTypography.headlineSmall
            )
        })
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ProfileTabs(user: User) {
    val tabs = listOf(
        TabItem.Profile.Details(user),
        TabItem.Profile.Activity(user),
        TabItem.Profile.Settings(user)
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ScrollableTabs(tabs = tabs, pagerState = pagerState, coroutineScope)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}