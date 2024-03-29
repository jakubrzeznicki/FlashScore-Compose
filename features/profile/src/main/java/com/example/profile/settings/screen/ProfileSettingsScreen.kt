package com.example.profile.settings.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.navigation.SignUpBackStackType
import com.example.data.navigation.WelcomeBackStackType
import com.example.profile.R
import com.example.profile.navigation.ProfileNavigator
import com.example.profile.settings.model.ProfileSettingsError
import com.example.profile.settings.model.ProfileSettingsUiState
import com.example.profile.settings.viewmodel.ProfileSettingsViewModel
import com.example.ui.composables.*
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import com.example.ui.R as uiR

/**
 * Created by jrzeznicki on 09/02/2023.
 */

private const val PROFILE_SETTINGS_KEY = "PROFILE_SETTINGS_KEY"

@Composable
fun ProfileSettingsScreen(
    userId: String,
    navigator: ProfileNavigator,
    viewModel: ProfileSettingsViewModel = getViewModel { parametersOf(userId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = PROFILE_SETTINGS_KEY) { viewModel.setup() }
    SettingsScreen(
        uiState = uiState,
        focusManager = focusManager,
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onRepeatPasswordChange = { viewModel.onRepeatPasswordChange(it) },
        togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        toggleRepeatPasswordVisibility = { viewModel.toggleRepeatPasswordVisibility() },
        onPasswordClick = { viewModel.onPasswordClick() },
        onSignInClick = { navigator.openSignIn() },
        onSignUpClick = { navigator.openSignUp(SignUpBackStackType.Anonymous) },
        onDeleteAccountClick = {
            viewModel.onDeleteAccountClick { navigator.openWelcome(WelcomeBackStackType.Profile) }
        },
        onSavePasswordClick = { viewModel.onSavePasswordClick() },
        onSignOutClick = {
            viewModel.onSignOutClick { navigator.openWelcome(WelcomeBackStackType.Profile) }
        },
        showDeleteAccountDialog = { viewModel.showConfirmDeleteAccountDialog(it) },
        showSignOutDialog = { viewModel.showConfirmSignOutDialog(it) }
    )
}

@Composable
private fun SettingsScreen(
    uiState: ProfileSettingsUiState,
    focusManager: FocusManager,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    togglePasswordVisibility: () -> Unit,
    toggleRepeatPasswordVisibility: () -> Unit,
    onPasswordClick: () -> Unit,
    onSavePasswordClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onSignOutClick: () -> Unit,
    showDeleteAccountDialog: (Boolean) -> Unit,
    showSignOutDialog: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (uiState) {
                        is ProfileSettingsUiState.HasData -> {
                            if (uiState.user.isAnonymous) {
                                AnonymousSettings(onSignInClick, onSignUpClick)
                            } else {
                                PasswordCard(
                                    uiState = uiState,
                                    focusManager = focusManager,
                                    onPasswordChange = onPasswordChange,
                                    onRepeatPasswordChange = onRepeatPasswordChange,
                                    togglePasswordVisibility = togglePasswordVisibility,
                                    toggleRepeatPasswordVisibility = toggleRepeatPasswordVisibility,
                                    onPasswordClick = onPasswordClick,
                                    onSaveClick = onSavePasswordClick
                                )
                                SignInButton(
                                    uiState.shouldShowConfirmSignOutDialog,
                                    showSignOutDialog,
                                    onSignOutClick
                                )
                                DeleteAccountButton(
                                    uiState.shouldShowConfirmDeleteAccountDialog,
                                    showDeleteAccountDialog,
                                    onDeleteAccountClick
                                )
                            }
                        }
                        is ProfileSettingsUiState.NoData -> {
                            EmptyState(
                                modifier = Modifier.fillMaxWidth(),
                                textId = R.string.no_profile_settings
                            )
                        }
                    }
                }
            }
        }
        CircularProgressBar(uiState.isLoading)
    }
}

@Composable
private fun SignInButton(
    shouldShowConfirmSignOutDialog: Boolean,
    showSignOutDialog: (Boolean) -> Unit,
    onSignOutClick: () -> Unit
) {
    Divider(
        color = MaterialTheme.colorScheme.inverseSurface,
        thickness = 2.dp,
        modifier = Modifier.fillMaxWidth(0.8f)
    )
    OutlinedButton(
        onClick = { showSignOutDialog(true) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Text(text = stringResource(id = R.string.sign_out))
    }
    if (shouldShowConfirmSignOutDialog) {
        InformationDialog(
            titleId = R.string.sign_out_title,
            textId = R.string.sign_out_description,
            confirmButtonTextId = R.string.sign_out,
            confirmButtonAction = onSignOutClick,
            dismissAction = showSignOutDialog
        )
    }
    Spacer(modifier = Modifier.size(8.dp))
}

@Composable
private fun DeleteAccountButton(
    shouldShowConfirmDeleteAccountDialog: Boolean,
    showDeleteAccountDialog: (Boolean) -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    Divider(
        color = MaterialTheme.colorScheme.inverseSurface,
        thickness = 2.dp,
        modifier = Modifier.fillMaxWidth(0.8f)
    )
    OutlinedButton(
        onClick = { showDeleteAccountDialog(true) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text(text = stringResource(id = R.string.delete_account))
    }
    if (shouldShowConfirmDeleteAccountDialog) {
        InformationDialog(
            titleId = R.string.delete_account_title,
            textId = R.string.delete_account_description,
            confirmButtonTextId = R.string.delete_account,
            confirmButtonAction = onDeleteAccountClick,
            dismissAction = showDeleteAccountDialog
        )
    }
}

@Composable
private fun AnonymousSettings(onSignInClick: () -> Unit, onSignUpClick: () -> Unit) {
    OutlinedButton(
        onClick = onSignInClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text(text = stringResource(id = com.example.ui.R.string.sign_in))
    }
    OutlinedButton(
        onClick = onSignUpClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.tertiary
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text(text = stringResource(id = com.example.ui.R.string.create_account))
    }
}

@Composable
fun PasswordCard(
    uiState: ProfileSettingsUiState,
    focusManager: FocusManager,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    togglePasswordVisibility: () -> Unit,
    toggleRepeatPasswordVisibility: () -> Unit,
    onPasswordClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable { onPasswordClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.change_password),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            ProfileSettingsItemIcon(
                icon = Icons.Default.Key,
                onPasswordClick = onPasswordClick
            )
        }
    }
    AnimatedVisibility(visible = uiState.isPasswordCardExpanded) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val errorMessage = when (uiState.error) {
                is ProfileSettingsError.InvalidPassword -> stringResource(uiR.string.password_error)
                is ProfileSettingsError.NotMatchesPassword ->
                    stringResource(uiR.string.password_match_error)
                is ProfileSettingsError.EmptyProfile -> stringResource(R.string.no_profile_settings)
                else -> null
            }
            PasswordTextField(
                labelId = R.string.new_password,
                value = uiState.password,
                onValueChange = onPasswordChange,
                trailingIcon = {
                    ToggleTextVisibilityTrailingButton(
                        onClick = togglePasswordVisibility,
                        isVisible = uiState.isPasswordVisible
                    )
                },
                errorMessage = errorMessage,
                hideText = !uiState.isPasswordVisible,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextField(
                labelId = R.string.repeat_new_password,
                value = uiState.repeatPassword,
                onValueChange = onRepeatPasswordChange,
                trailingIcon = {
                    ToggleTextVisibilityTrailingButton(
                        onClick = toggleRepeatPasswordVisibility,
                        isVisible = uiState.isRepeatPasswordVisible
                    )
                },
                errorMessage = errorMessage,
                hideText = !uiState.isRepeatPasswordVisible,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSaveClick()
                    }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onSaveClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = stringResource(id = R.string.save)
                )
            }
            if (uiState.error is ProfileSettingsError.AuthenticationError) {
                val authenticationError =
                    (uiState.error as ProfileSettingsError.AuthenticationError)
                        .responseStatus.statusMessage.orEmpty()
                TextFieldError((authenticationError))
            }
        }
    }
}

@Composable
private fun ProfileSettingsItemIcon(
    icon: ImageVector,
    onPasswordClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(shape = CircleShape, color = MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.inverseSurface
            )
            .size(40.dp)
            .clickable { onPasswordClick() },
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .align(Alignment.Center)
                .padding(8.dp),
            onClick = { onPasswordClick() }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
private fun InformationDialog(
    titleId: Int,
    textId: Int,
    confirmButtonTextId: Int,
    confirmButtonAction: () -> Unit,
    dismissAction: (Boolean) -> Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        title = { Text(stringResource(titleId)) },
        text = { Text(stringResource(textId)) },
        dismissButton = {
            Button(
                onClick = { dismissAction(false) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    confirmButtonAction()
                    dismissAction(false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = stringResource(confirmButtonTextId))
            }
        },
        onDismissRequest = { dismissAction(false) }
    )
}
