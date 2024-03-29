package com.example.signin.singup.screen

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.navigation.SignUpBackStackType
import com.example.data.navigation.WelcomeBackStackType
import com.example.signin.R
import com.example.signin.navigation.SignInNavigator
import com.example.signin.singup.model.SignUpError
import com.example.signin.singup.model.SignUpUiState
import com.example.signin.singup.viewmodel.SignUpViewModel
import com.example.ui.composables.*
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 05/02/2023.
 */

@Destination
@Composable
fun SignUpRoute(
    signUpBackStackType: SignUpBackStackType,
    navigator: SignInNavigator,
    viewModel: SignUpViewModel = getViewModel { parametersOf(signUpBackStackType) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
            viewModel.onSignUpClick { navigator.openWelcome(WelcomeBackStackType.SignUp) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreen(
    modifier: Modifier = Modifier,
    navigator: SignInNavigator? = null,
    uiState: SignUpUiState,
    focusManager: FocusManager,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRepeatPasswordChange: (String) -> Unit = {},
    togglePasswordVisibility: () -> Unit = {},
    toggleRepeatPasswordVisibility: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopBar(navigator = navigator) }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
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
                    labelId = com.example.ui.R.string.password,
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
                        text = stringResource(id = com.example.ui.R.string.create_account)
                    )
                }
                if (uiState.error is SignUpError.AuthenticationError) {
                    TextFieldError(uiState.error.responseStatus.statusMessage.orEmpty())
                }
            }
            CircularProgressBar(uiState.isLoading)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: SignInNavigator?) {
    CenterAppTopBar(
        modifier = Modifier
            .height(58.dp)
            .padding(top = 8.dp),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(32.dp),
                onClick = { navigator?.navigateUp() }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        title = {}
    )
}
