package com.kuba.flashscorecompose.signin.screen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.HomeScreenRouteDestination
import com.kuba.flashscorecompose.destinations.SignInRouteDestination
import com.kuba.flashscorecompose.signin.model.SignInUiState
import com.kuba.flashscorecompose.signin.viewmodel.SignInViewModel
import com.kuba.flashscorecompose.ui.component.CenterAppTopBar
import com.kuba.flashscorecompose.ui.component.EmailField
import com.kuba.flashscorecompose.ui.component.NormalPasswordField
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 05/02/2023.
 */

@Destination
@Composable
fun SignInRoute(
    navigator: DestinationsNavigator,
    viewModel: SignInViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    SignInScreen(
        uiState = uiState,
        navigator = navigator,
        scope = scope,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onSignInClick = {
            viewModel.onSignInClick {
                navigator.navigate(HomeScreenRouteDestination()) {
                    popUpTo(SignInRouteDestination.route) { inclusive = true }
                }
            }
        },
        onForgetPasswordClick = { viewModel.onForgotPasswordClick() },
        onErrorClear = {},
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator? = null,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    uiState: SignInUiState = SignInUiState(email = "kermit@gmail.com", "Jurek123"),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onSignInClick: () -> Unit = {},
    onForgetPasswordClick: () -> Unit = {},
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
                // state = uiState.emailTextFieldState,
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
            Button(
                onClick = { onSignInClick() },
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
                    text = stringResource(id = R.string.sign_in)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = {
                    onForgetPasswordClick()
                    scope.launch {
//                        snackbarHostState.showSnackbar(
//                            message = snackbarErrorText,
//                            actionLabel = snackbarActionLabel
//                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Companion.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(text = stringResource(id = R.string.forgot_password))
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