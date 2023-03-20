package com.kuba.flashscorecompose.welcome.screen

import androidx.annotation.StringRes
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.HomeScreenRouteDestination
import com.kuba.flashscorecompose.destinations.OnBoardingRputeDestination
import com.kuba.flashscorecompose.destinations.SignInRouteDestination
import com.kuba.flashscorecompose.destinations.SignUpRouteDestination
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.kuba.flashscorecompose.welcome.model.WelcomeError
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
        onSignUpClick = { navigator.navigate(SignUpRouteDestination()) },
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
        when (uiState.error) {
            is WelcomeError.NoError -> {
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
            }
            is WelcomeError.AuthenticationError -> {
                Text(text = stringResource(R.string.generic_error))
                BasicButton(
                    R.string.try_again,
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                ) { onSignInAsGuest() }
            }
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

@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    Button(
        onClick = action,
        modifier = modifier,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = stringResource(text), fontSize = 16.sp)
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    FlashScoreComposeTheme {
        // WelcomeScreen()
    }
}
