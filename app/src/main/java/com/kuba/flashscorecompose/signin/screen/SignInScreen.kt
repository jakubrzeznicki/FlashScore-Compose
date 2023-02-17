package com.kuba.flashscorecompose.signin.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.HomeScreenRouteDestination
import com.kuba.flashscorecompose.destinations.OnBoardingRputeDestination
import com.kuba.flashscorecompose.destinations.SignInRouteDestination
import com.kuba.flashscorecompose.signin.model.SignInError
import com.kuba.flashscorecompose.signin.model.SignInUiState
import com.kuba.flashscorecompose.signin.viewmodel.SignInViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    SignInScreen(
        uiState = uiState,
        navigator = navigator,
        focusManager = focusManager,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        toggleKeepLogged = { viewModel.toggleKeepLogged() },
        onSignInClick = {
            viewModel.onSignInClick(
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
        },
        onForgetPasswordClick = { viewModel.onForgotPasswordClick() }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator? = null,
    focusManager: FocusManager,
    uiState: SignInUiState,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    togglePasswordVisibility: () -> Unit = {},
    toggleKeepLogged: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onForgetPasswordClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopBar(navigator = navigator) }
    ) {
        val scrollState = rememberScrollState()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 48.dp, start = 32.dp, end = 32.dp)
                    .verticalScroll(scrollState)
            ) {
                EmailTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    errorMessage = when (uiState.error) {
                        is SignInError.InvalidEmail -> stringResource(id = uiState.error.messageId)
                        else -> null
                    },
                    onKeyBoardAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                PasswordTextField(
                    labelId = R.string.password,
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    trailingIcon = {
                        ToggleTextVisibilityTrailingButton(
                            onClick = togglePasswordVisibility,
                            isVisible = uiState.isPasswordVisible
                        )
                    },
                    errorMessage = when (uiState.error) {
                        is SignInError.BlankPassword -> stringResource(id = uiState.error.messageId)
                        else -> null
                    },
                    hideText = !uiState.isPasswordVisible,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onSignInClick()
                        }
                    )
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
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = stringResource(id = R.string.sign_in)
                    )
                }
                if (uiState.error is SignInError.AuthenticationError) {
                    TextFieldError(uiState.error.responseStatus.statusMessage.orEmpty())
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = { onForgetPasswordClick() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Companion.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(text = stringResource(id = R.string.forgot_password))
                }
            }
            CircularProgressBar(uiState.isLoading)
        }
    }
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
        title = {}
    )
}