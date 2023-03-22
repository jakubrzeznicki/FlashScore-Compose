package com.example.welcome.welcome.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.navigation.HomeBackStackType
import com.example.data.navigation.SignInBackStackType
import com.example.data.navigation.SignUpType
import com.example.ui.composables.CircularProgressBar
import com.example.welcome.R
import com.example.welcome.navigation.WelcomeNavigator
import com.example.welcome.welcome.model.WelcomeUiState
import com.example.welcome.welcome.viewmodel.WelcomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 06/02/2023.
 */
@Destination
@Composable
fun WelcomeRoute(
    navigator: WelcomeNavigator,
    viewModel: WelcomeViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WelcomeScreen(
        uiState = uiState,
        onSignInClick = { navigator.openSignIn(SignInBackStackType.Welcome) },
        onSignUpClick = { navigator.openSignUp(SignUpType.New) },
        onSignInAsGuest = {
            viewModel.createAnonymousAccount(
                openHomeScreen = { navigator.openHome(HomeBackStackType.Welcome) },
                openOnBoardingScreen = { navigator.openOnBoarding() }
            )
        }
    )
}

@Composable
fun WelcomeScreen(
    uiState: WelcomeUiState,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignInAsGuest: () -> Unit
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
                    .verticalScroll(scrollState)
            ) {
                BrandingImage()
                SignButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 16.dp),
                    onSignInClick = onSignInClick,
                    onSignUpClick = onSignUpClick
                )
                SignInAsGuestButton(
                    modifier = Modifier.fillMaxWidth(),
                    onSignInAsGuest = onSignInAsGuest
                )
            }
            CircularProgressBar(uiState.isLoading)
        }
    }
}

@Composable
fun BrandingImage() {
    Column {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(top = 24.dp),
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.mbappe),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = stringResource(id = R.string.welcome_title),
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(id = R.string.welcome_subtitle),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SignButtons(
    modifier: Modifier,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit
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
                text = stringResource(id = com.example.ui.R.string.sign_in)
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
fun SignInAsGuestButton(
    modifier: Modifier = Modifier,
    onSignInAsGuest: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.or),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        OutlinedButton(
            onClick = onSignInAsGuest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            Text(text = stringResource(id = R.string.sign_in_guest))
        }
    }
}
