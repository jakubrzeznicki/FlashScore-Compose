package com.kuba.flashscorecompose.profile.settings.screen

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
import androidx.compose.runtime.collectAsState
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
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.ProfileRouteDestination
import com.kuba.flashscorecompose.destinations.SignInRouteDestination
import com.kuba.flashscorecompose.destinations.SignUpRouteDestination
import com.kuba.flashscorecompose.destinations.WelcomeRouteDestination
import com.kuba.flashscorecompose.profile.settings.model.ProfileSettingsItem
import com.kuba.flashscorecompose.profile.settings.model.ProfileSettingsUiState
import com.kuba.flashscorecompose.profile.settings.viewmodel.ProfileSettingsViewModel
import com.kuba.flashscorecompose.signup.model.SignUpError
import com.kuba.flashscorecompose.ui.component.PasswordTextField
import com.kuba.flashscorecompose.ui.component.TextFieldError
import com.kuba.flashscorecompose.ui.component.ToggleTextVisibilityTrailingButton
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 09/02/2023.
 */

private const val PROFILE_SETTINGS_KEY = "PROFILE_SETTINGS_KEY"

@Composable
fun ProfileSettingsScreen(
    userId: String,
    navigator: DestinationsNavigator,
    viewModel: ProfileSettingsViewModel = getViewModel { parametersOf(userId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = PROFILE_SETTINGS_KEY) { viewModel.setup() }
    SettingsScreen(
        uiState = uiState,
        focusManager = focusManager,
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onRepeatPasswordChange = { viewModel.onRepeatPasswordChange(it) },
        togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        toggleRepeatPasswordVisibility = { viewModel.toggleRepeatPasswordVisibility() },
        onCardClick = { viewModel.onCardClick(it) },
        onSignInClick = { navigator.navigate(SignInRouteDestination()) },
        onSignUpClick = { navigator.navigate(SignUpRouteDestination()) },
        onDeleteAccountClick = {
            viewModel.onDeleteAccountClick {
                navigator.navigate(WelcomeRouteDestination()) {
                    popUpTo(ProfileRouteDestination.route) { inclusive = true }
                }
            }
        },
        onSavePasswordClick = { viewModel.onSavePasswordClick() },
        onSignOutClick = {
            viewModel.onSignOutClick {
                navigator.navigate(WelcomeRouteDestination()) {
                    popUpTo(ProfileRouteDestination.route) { inclusive = true }
                }
            }
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
    onCardClick: (ProfileSettingsItem) -> Unit,
    onSavePasswordClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onSignOutClick: () -> Unit,
    showDeleteAccountDialog: (Boolean) -> Unit,
    showSignOutDialog: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.user.isAnonymous) {
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
                        Text(text = stringResource(id = R.string.sign_in))
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
                        Text(text = stringResource(id = R.string.create_account))
                    }
                } else {
                    PasswordCard(
                        uiState = uiState,
                        focusManager = focusManager,
                        onPasswordChange = onPasswordChange,
                        onRepeatPasswordChange = onRepeatPasswordChange,
                        togglePasswordVisibility = togglePasswordVisibility,
                        toggleRepeatPasswordVisibility = toggleRepeatPasswordVisibility,
                        onCardClick = onCardClick,
                        onSaveClick = onSavePasswordClick
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
                    )
                    OutlinedButton(
                        onClick = { showSignOutDialog(true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                    ) {
                        Text(text = stringResource(id = R.string.sign_out))
                    }
                    if (uiState.shouldShowConfirmSignOutDialog) {
                        InformationDialog(
                            titleId = R.string.sign_out_title,
                            textId = R.string.sign_out_description,
                            confirmButtonTextId = R.string.sign_out,
                            confirmButtonAction = onSignOutClick,
                            dismissAction = showSignOutDialog
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Divider(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
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
                    if (uiState.shouldShowConfirmDeleteAccountDialog) {
                        InformationDialog(
                            titleId = R.string.delete_account_title,
                            textId = R.string.delete_account_description,
                            confirmButtonTextId = R.string.delete_account,
                            confirmButtonAction = onDeleteAccountClick,
                            dismissAction = showDeleteAccountDialog
                        )
                    }
                }
            }
        }
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
    onCardClick: (ProfileSettingsItem) -> Unit,
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
            .clickable { onCardClick(ProfileSettingsItem.Password) }
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
                profileSettingsItem = ProfileSettingsItem.Password,
                onCardClick = onCardClick
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
            Spacer(modifier = Modifier.height(8.dp))
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
                // enabled = emailState.isValid && passwordState.isValid
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = stringResource(id = R.string.save)
                )
            }
            if (uiState.error is SignUpError.AuthenticationError) {
                TextFieldError(uiState.error.responseStatus.statusMessage.orEmpty())
            }
        }
    }
}

@Composable
private fun ProfileSettingsItemIcon(
    icon: ImageVector,
    profileSettingsItem: ProfileSettingsItem,
    onCardClick: (ProfileSettingsItem) -> Unit
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
            .clickable { onCardClick(profileSettingsItem) },
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .align(Alignment.Center)
                .padding(8.dp),
            onClick = { onCardClick(profileSettingsItem) }
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