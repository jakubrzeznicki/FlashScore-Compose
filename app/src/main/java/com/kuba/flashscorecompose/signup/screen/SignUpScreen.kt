package com.kuba.flashscorecompose.signup.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.SignUpRouteDestination
import com.kuba.flashscorecompose.destinations.WelcomeRouteDestination
import com.kuba.flashscorecompose.signin.screen.SignInScreen
import com.kuba.flashscorecompose.signup.model.SignUpUiState
import com.kuba.flashscorecompose.signup.viewmodel.SignUpViewModel
import com.kuba.flashscorecompose.ui.component.CenterAppTopBar
import com.kuba.flashscorecompose.ui.component.EmailField
import com.kuba.flashscorecompose.ui.component.NormalPasswordField
import com.kuba.flashscorecompose.ui.component.RepeatPasswordField
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 05/02/2023.
 */

@Destination
@Composable
fun SignUpRoute(
    navigator: DestinationsNavigator,
    viewModel: SignUpViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    SignUpScreen(
        uiState = uiState,
        navigator = navigator,
        scope = scope,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onRepeatPasswordChange = { viewModel.onRepeatPasswordChange(it) },
        onSignUpClick = {
            viewModel.onSignUpClick {
                navigator.navigate(WelcomeRouteDestination()) {
                    popUpTo(SignUpRouteDestination.route) { inclusive = true }
                }
            }
        },
        onErrorClear = {}
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator? = null,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    uiState: SignUpUiState = SignUpUiState(email = "kermit@gmail.com", "Jurek123", "Jurek123"),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRepeatPasswordChange: (String) -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onErrorClear: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopBar(navigator = navigator) }
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 32.dp, end = 32.dp)
                .verticalScroll(scrollState)
        ) {
            val focusRequester = remember { FocusRequester() }
            EmailField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                value = uiState.email,
                onNewValue = onEmailChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            NormalPasswordField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                value = uiState.password,
                onNewValue = onPasswordChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            RepeatPasswordField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                value = uiState.repeatPassword,
                onNewValue = onRepeatPasswordChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onSignUpClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                shape = RoundedCornerShape(16.dp)
                // enabled = emailState.isValid && passwordState.isValid
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = stringResource(id = R.string.create_account)
                )
            }
        }
    }
//    ErrorSnackbar(
//        snackbarHostState = snackbarHostState,
//        onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
//        modifier = Modifier.align(Alignment.BottomCenter)
//    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: DestinationsNavigator?) {
    CenterAppTopBar(
        modifier = Modifier
            .height(58.dp)
            .padding(vertical = 8.dp),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(24.dp),
                onClick = { navigator?.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        title = {
        }
    )
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun ErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                action = {
                    data.visuals.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

@Preview
@Composable
fun SignInScreenPreview() {
    FlashScoreComposeTheme {
        SignInScreen()
    }
}