package com.kuba.flashscorecompose.profile.details.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.profile.details.model.ProfileDetailsUiState
import com.kuba.flashscorecompose.profile.details.model.ProfileItem
import com.kuba.flashscorecompose.profile.details.viewmodel.ProfileDetailsViewModel
import com.kuba.flashscorecompose.ui.component.CircularProgressBar
import com.kuba.flashscorecompose.ui.component.ProfileTextField
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 08/02/2023.
 */

private const val PROFILE_DETAILS_KEY = "PROFILE_DETAILS_KEY"

@Composable
fun ProfileDetailsScreen(viewModel: ProfileDetailsViewModel = getViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = PROFILE_DETAILS_KEY) { viewModel.setup() }
    DetailsScreen(
        uiState = uiState,
        onItemClick = { viewModel.onItemClick(it) },
        onDoneClick = { viewModel.onDoneClick(it) },
        onValueChange = { viewModel.onValueChange(it) },
        clearFocus = { focusManager.clearFocus() }
    )
}

@Composable
private fun DetailsScreen(
    uiState: ProfileDetailsUiState,
    onItemClick: (ProfileItem) -> Unit,
    onDoneClick: (ProfileItem) -> Unit,
    onValueChange: (ProfileItem) -> Unit,
    clearFocus: () -> Unit
) {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 16.dp)
        ) {
            ProfileItemWithDivider(
                isExpanded = uiState.isNameExpanded,
                icon = Icons.Filled.Person,
                label = R.string.name,
                profileItem = ProfileItem.Name(value = uiState.user.name),
                keyboardType = KeyboardType.Text,
                onValueChange = onValueChange,
                onItemClick = onItemClick,
                onDoneClick = onDoneClick,
                clearFocus = clearFocus
            )
            ProfileItemWithDivider(
                isExpanded = uiState.isEmailExpanded,
                icon = Icons.Filled.Mail,
                label = R.string.email,
                profileItem = ProfileItem.Email(value = uiState.user.email),
                keyboardType = KeyboardType.Email,
                onValueChange = onValueChange,
                onItemClick = onItemClick,
                onDoneClick = onDoneClick,
                clearFocus = clearFocus
            )
            ProfileItemWithDivider(
                isExpanded = uiState.isPhoneExpanded,
                icon = Icons.Filled.Call,
                label = R.string.phone,
                profileItem = ProfileItem.Phone(value = uiState.user.phone),
                keyboardType = KeyboardType.Phone,
                onValueChange = onValueChange,
                onItemClick = onItemClick,
                onDoneClick = onDoneClick,
                clearFocus = clearFocus
            )
            ProfileItemWithDivider(
                isExpanded = uiState.isAddressExpanded,
                icon = Icons.Filled.LocationOn,
                label = R.string.address,
                profileItem = ProfileItem.Address(value = uiState.user.address),
                keyboardType = KeyboardType.Text,
                onValueChange = onValueChange,
                onItemClick = onItemClick,
                onDoneClick = onDoneClick,
                clearFocus = clearFocus
            )
        }
        CircularProgressBar(uiState.isLoading)
    }
}

@Composable
private fun ProfileItemWithDivider(
    isExpanded: Boolean,
    icon: ImageVector,
    label: Int,
    keyboardType: KeyboardType,
    profileItem: ProfileItem,
    onItemClick: (ProfileItem) -> Unit,
    onDoneClick: (ProfileItem) -> Unit,
    onValueChange: (ProfileItem) -> Unit,
    clearFocus: () -> Unit
) {
    Column {
        ProfileItemRow(
            modifier = Modifier.fillMaxWidth(),
            isEditing = isExpanded,
            icon = icon,
            label = label,
            profileItem = profileItem,
            keyboardType = keyboardType,
            onValueChange = onValueChange,
            onItemClick = onItemClick,
            onDoneClick = onDoneClick,
            clearFocus = clearFocus
        )
        Divider(
            color = MaterialTheme.colorScheme.surface,
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ProfileItemRow(
    modifier: Modifier,
    label: Int,
    isEditing: Boolean,
    icon: ImageVector = Icons.Filled.Person,
    profileItem: ProfileItem,
    keyboardType: KeyboardType = KeyboardType.Text,
    onItemClick: (ProfileItem) -> Unit,
    onDoneClick: (ProfileItem) -> Unit,
    onValueChange: (ProfileItem) -> Unit,
    clearFocus: () -> Unit
) {
    val rotationState by animateFloatAsState(targetValue = if (isEditing) 180f else 0f)
    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable { onItemClick(profileItem) }
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileItemIcon(icon = icon, profileItem = profileItem, onItemClick = onItemClick)
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(
                        text = stringResource(id = label),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = profileItem.value,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            IconButton(
                onClick = { onItemClick(profileItem) },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp)
                    .rotate(rotationState)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
        AnimatedVisibility(visible = isEditing) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ProfileTextField(
                    modifier = Modifier.weight(1f),
                    labelId = label,
                    profileItem = profileItem,
                    onValueChange = onValueChange,
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(id = label)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            clearFocus()
                            onDoneClick(profileItem)
                            onItemClick(profileItem)
                        }
                    )
                )
                IconButton(
                    onClick = {
                        onDoneClick(profileItem)
                        onItemClick(profileItem)
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(32.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Filled.Save,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileItemIcon(
    icon: ImageVector,
    profileItem: ProfileItem,
    onItemClick: (ProfileItem) -> Unit
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
            .clickable { onItemClick(profileItem) },
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .align(Alignment.Center)
                .padding(8.dp),
            onClick = { onItemClick(profileItem) }
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
