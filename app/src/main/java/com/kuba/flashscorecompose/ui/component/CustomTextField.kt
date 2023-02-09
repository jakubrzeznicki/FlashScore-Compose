package com.kuba.flashscorecompose.ui.component


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.profile.details.model.ProfileItem

/**
 * Created by jrzeznicki on 06/02/2023.
 */

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int = R.string.email,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Filled.Mail,
            contentDescription = stringResource(id = R.string.email)
        )
    },
    trailingIcon: @Composable (() -> Unit)? = null,
    hideText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next
    ),
    onKeyBoardAction: () -> Unit
) {
    CustomTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        labelId = labelId,
        errorMessage = errorMessage,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        hideText = hideText,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = {
                onKeyBoardAction()
            }
        )
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Filled.Key,
            contentDescription = stringResource(id = R.string.password)
        )
    },
    trailingIcon: @Composable (() -> Unit)?,
    hideText: Boolean = false,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
) {
    CustomTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        labelId = labelId,
        errorMessage = errorMessage,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        hideText = hideText,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTextField(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int,
    profileItem: ProfileItem,
    onValueChange: (ProfileItem) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    hideText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(modifier = modifier) {
        val visualTransformation =
            if (hideText) PasswordVisualTransformation() else VisualTransformation.None
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = profileItem.value,
            onValueChange = {
                when (profileItem) {
                    is ProfileItem.Name -> onValueChange(ProfileItem.Name(it))
                    is ProfileItem.Email -> onValueChange(ProfileItem.Email(it))
                    is ProfileItem.Phone -> onValueChange(ProfileItem.Phone(it))
                    is ProfileItem.Address -> onValueChange(ProfileItem.Address(it))
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.inverseOnSurface,
                cursorColor = MaterialTheme.colorScheme.inverseOnSurface,
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
                placeholderColor = MaterialTheme.colorScheme.inverseOnSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.inverseOnSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.inverseOnSurface,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.inverseOnSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.inverseOnSurface,
                errorLeadingIconColor = MaterialTheme.colorScheme.error
            ),
            label = {
                Text(
                    text = stringResource(id = labelId),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            shape = RoundedCornerShape(16.dp),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    hideText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(modifier = modifier) {
        val visualTransformation =
            if (hideText) PasswordVisualTransformation() else VisualTransformation.None
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { onValueChange(it) },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.inverseOnSurface,
                cursorColor = MaterialTheme.colorScheme.inverseOnSurface,
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
                placeholderColor = MaterialTheme.colorScheme.inverseOnSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.inverseOnSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.inverseOnSurface,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.inverseOnSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.inverseOnSurface,
                errorLeadingIconColor = MaterialTheme.colorScheme.error
            ),
            label = {
                Text(
                    text = stringResource(id = labelId),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            shape = RoundedCornerShape(16.dp),
            isError = errorMessage != null,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        errorMessage?.let {
            TextFieldError(it)
        }
    }
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun ToggleTextVisibilityTrailingButton(
    onClick: () -> Unit,
    isVisible: Boolean
) = IconButton(onClick = onClick) {
    Icon(
        imageVector = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
        contentDescription = stringResource(
            id = if (isVisible) R.string.hide_password else R.string.show_password
        )
    )
}