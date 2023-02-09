package com.kuba.flashscorecompose.profile.container.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.*
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.profile.container.model.ProfileUiState
import com.kuba.flashscorecompose.profile.container.viewmodel.ProfileViewModel
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.component.tabs.ScrollableTabs
import com.kuba.flashscorecompose.ui.component.tabs.TabItem
import com.kuba.flashscorecompose.ui.component.tabs.TabsContent
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.kuba.flashscorecompose.utils.toast
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 08/02/2023.
 */
private const val SETUP_PROFILE_KEY = "SETUP_PROFILE_KEY"
private const val IMAGE_PATH = "image/*"

@Destination
@Composable
fun ProfileRoute(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current.applicationContext
    LaunchedEffect(key1 = SETUP_PROFILE_KEY) { viewModel.setup() }
    ProfileScreen(
        context = context,
        uiState = uiState,
        onPhotoUriPicked = { viewModel.updatePhoto(it) }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    context: Context,
    uiState: ProfileUiState,
    onPhotoUriPicked: (Uri) -> Unit
) {
    Scaffold(
        topBar = { TopBar() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
        ) {
            ProfileHeader(
                context,
                uiState.user.name,
                Uri.parse(uiState.user.photoUri),
                onPhotoUriPicked
            )
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

@Composable
fun ProfileHeader(
    context: Context,
    name: String,
    userPhotoUri: Uri,
    onPhotoUriPicked: (Uri) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileImage(context, userPhotoUri, onPhotoUriPicked)
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileImage(context: Context, userPhotoUri: Uri, onPhotoUriPicked: (Uri) -> Unit) {
    val photoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                Log.d("TEST_LOG", "selected uri -  $it")
                onPhotoUriPicked(it)
            }
        }
    val permissions = getPermissions()
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { wasGranted ->
            if (wasGranted) {
                context.toast(R.string.permission_granted)
                photoLauncher.launch(arrayOf(IMAGE_PATH))
            } else {
                context.toast(R.string.permission_denied)
            }
        }
    Box(
        modifier = Modifier
            .background(shape = CircleShape, color = MaterialTheme.colorScheme.surface)
            .size(112.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .align(Alignment.Center)
                .size(112.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data(userPhotoUri)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_profile),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
        ProfileItemIcon(
            modifier = Modifier.align(Alignment.BottomEnd),
            context = context,
            permissions = permissions,
            photoLauncher = photoLauncher,
            permissionLauncher = permissionLauncher
        )
    }
}


@Composable
private fun ProfileItemIcon(
    modifier: Modifier,
    context: Context,
    permissions: String,
    photoLauncher: ManagedActivityResultLauncher<Array<String>, Uri?>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    Box(
        modifier = modifier
            .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary)
            .border(
                width = 2.dp,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface
            )
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                val permissionCheckResult = ContextCompat.checkSelfPermission(context, permissions)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    photoLauncher.launch(arrayOf(IMAGE_PATH))
                } else {
                    permissionLauncher.launch(permissions)
                }
            },
            modifier = Modifier.clip(CircleShape)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Filled.Edit,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

private fun getPermissions(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_IMAGES
} else {
    Manifest.permission.READ_EXTERNAL_STORAGE
}