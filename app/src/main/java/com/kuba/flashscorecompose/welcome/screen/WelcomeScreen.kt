package com.kuba.flashscorecompose.welcome.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.HomeScreenRouteDestination
import com.kuba.flashscorecompose.destinations.OnBoardingRputeDestination
import com.kuba.flashscorecompose.destinations.SignInRouteDestination
import com.kuba.flashscorecompose.destinations.SignUpRouteDestination
import com.kuba.flashscorecompose.signup.model.SignUpType
import com.kuba.flashscorecompose.ui.component.CircularProgressBar
import com.kuba.flashscorecompose.welcome.model.WelcomeUiState
import com.kuba.flashscorecompose.welcome.viewmodel.WelcomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 06/02/2023.
 */
@Destination
@Composable
fun WelcomeRoute(
    navigator: DestinationsNavigator,
    viewModel: WelcomeViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    WelcomeScreen(
        uiState = uiState,
        onSignInClick = { navigator.navigate(SignInRouteDestination()) },
        onSignUpClick = { SignUpRouteDestination(SignUpType.New) },
        onSignInAsGuest = {
            viewModel.createAnonymousAccount(
                openHomeScreen = {
                    navigator.navigate(HomeScreenRouteDestination()) {
                        popUpTo(SignInRouteDestination.route) { inclusive = true }
                    }
                },
                openOnBoardingScreen = {
                    navigator.navigate(OnBoardingRputeDestination()) {
                        popUpTo(SignInRouteDestination.route) { inclusive = true }
                    }
                }
            )
        }
    )
}

@Composable
fun WelcomeScreen(
    uiState: WelcomeUiState,
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onSignInAsGuest: () -> Unit = {}
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentWidth(align = Alignment.CenterHorizontally),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollState = rememberScrollState()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp)
                    .verticalScroll(scrollState),
            ) {
                Branding()
                SignInSignUpButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 16.dp),
                    onSignInClick = onSignInClick,
                    onSignUpClick = onSignUpClick
                )
                SignInAsGuest(
                    onSignInAsGuest = onSignInAsGuest,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            CircularProgressBar(uiState.isLoading)
        }
    }
}

@Composable
fun Branding() {
    Column {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(top = 24.dp),
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.mbappe),
            contentDescription = "",
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = stringResource(id = R.string.welcome_title),
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(id = R.string.welcome_subtitle),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun SignInSignUpButtons(
    modifier: Modifier,
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {
    Row(modifier = modifier) {
        Button(
            onClick = { onSignInClick() },
            modifier = Modifier.weight(5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = stringResource(id = R.string.sign_in)
            )
        }
        Button(
            onClick = { onSignUpClick() },
            modifier = Modifier.weight(3f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = stringResource(id = R.string.sign_up)
            )
        }
    }
}

@Composable
fun SignInAsGuest(
    onSignInAsGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.or),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        )
        OutlinedButton(
            onClick = onSignInAsGuest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            Text(text = stringResource(id = R.string.sign_in_guest))
        }
    }
}