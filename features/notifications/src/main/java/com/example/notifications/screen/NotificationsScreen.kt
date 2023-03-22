package com.example.notifications.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.notificationdata.NotificationData
import com.example.notifications.R
import com.example.notifications.model.NotificationsUiState
import com.example.notifications.navigation.NotificationsNavigator
import com.example.notifications.viewmodel.NotificationsViewModel
import com.example.ui.composables.CenterAppTopBar
import com.example.ui.composables.EmptyState
import com.example.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

private const val SETUP_NOTIFICATIONS_KEY = "SETUP_NOTIFICATIONS_KEY"

@Destination
@Composable
fun NotificationsRoute(
    navigator: NotificationsNavigator,
    viewModel: NotificationsViewModel = getViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = SETUP_NOTIFICATIONS_KEY) { viewModel.setup() }
    NotificationsScreen(
        uiState = uiState,
        context = context,
        navigator = navigator,
        onCancelClick = { viewModel.cancelNotification(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    uiState: NotificationsUiState,
    context: Context,
    navigator: NotificationsNavigator,
    onCancelClick: (NotificationData) -> Unit
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        topBar = { TopBar(navigator = navigator) }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            state = scrollState
        ) {
            when (uiState) {
                is NotificationsUiState.HasData -> {
                    items(items = uiState.notifications) {
                        NotificationItem(
                            notificationData = it,
                            onCancelClick = onCancelClick,
                            context = context
                        )
                        Divider(
                            color = MaterialTheme.colorScheme.inverseSurface,
                            thickness = 2.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                is NotificationsUiState.NoData -> {
                    item {
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            textId = com.example.notificationservice.R.string.no_notifications
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(8.dp),
                                painter = painterResource(id = com.example.ui.R.drawable.ic_close),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: NotificationsNavigator) {
    CenterAppTopBar(
        modifier = Modifier
            .height(48.dp)
            .padding(top = 8.dp),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(32.dp),
                onClick = { navigator.navigateUp() }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = com.example.ui.R.string.notifications),
                color = MaterialTheme.colorScheme.onSecondary,
                style = FlashScoreTypography.headlineSmall
            )
        }
    )
}

@Composable
private fun NotificationItem(
    notificationData: NotificationData,
    onCancelClick: (NotificationData) -> Unit,
    context: Context
) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Column {
                    Text(
                        text = stringResource(id = com.example.notificationservice.R.string.fixture),
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = "${notificationData.homeTeam} ${stringResource(id = com.example.notificationservice.R.string.vs)} ${notificationData.awayTeam}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = com.example.notificationservice.R.string.start),
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = notificationData.formattedDate,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = com.example.notificationservice.R.string.round),
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = notificationData.round,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = {
                    onCancelClick(notificationData)
                    Toast.makeText(
                        context,
                        com.example.notificationservice.R.string.cancel_notification,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = stringResource(id = R.string.cancel),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}
