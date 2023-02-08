package com.kuba.flashscorecompose.signup.screen

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.SignUpRouteDestination
import com.kuba.flashscorecompose.destinations.WelcomeRouteDestination
import com.kuba.flashscorecompose.signup.model.SignUpError
import com.kuba.flashscorecompose.signup.model.SignUpUiState
import com.kuba.flashscorecompose.signup.viewmodel.SignUpViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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
    val focusManager = LocalFocusManager.current
    SignUpScreen(
        uiState = uiState,
        navigator = navigator,
        focusManager = focusManager,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onRepeatPasswordChange = { viewModel.onRepeatPasswordChange(it) },
        togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        toggleRepeatPasswordVisibility = { viewModel.toggleRepeatPasswordVisibility() },
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
    uiState: SignUpUiState,
    focusManager: FocusManager,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRepeatPasswordChange: (String) -> Unit = {},
    togglePasswordVisibility: () -> Unit = {},
    toggleRepeatPasswordVisibility: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onErrorClear: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopBar(navigator = navigator) }
    ) {
        val scrollState = rememberScrollState()
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
                    is SignUpError.InvalidEmail -> stringResource(id = uiState.error.messageId)
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
                    is SignUpError.InvalidPassword -> stringResource(id = uiState.error.messageId)
                    is SignUpError.NotMatchesPassword -> stringResource(id = uiState.error.messageId)
                    else -> null
                },
                hideText = !uiState.isPasswordVisible,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                labelId = R.string.repeat_password,
                value = uiState.repeatPassword,
                onValueChange = onRepeatPasswordChange,
                trailingIcon = {
                    ToggleTextVisibilityTrailingButton(
                        onClick = toggleRepeatPasswordVisibility,
                        isVisible = uiState.isRepeatPasswordVisible
                    )
                },
                errorMessage = when (uiState.error) {
                    is SignUpError.InvalidPassword -> stringResource(id = uiState.error.messageId)
                    is SignUpError.NotMatchesPassword -> stringResource(id = uiState.error.messageId)
                    else -> null
                },
                hideText = !uiState.isRepeatPasswordVisible,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSignUpClick()
                    }
                )
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
            if (uiState.error is SignUpError.AuthenticationError) {
                TextFieldError(uiState.error.responseStatus.statusMessage.orEmpty())
            }
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
        title = {
        }
    )
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
        //  SignInScreen()
    }
}