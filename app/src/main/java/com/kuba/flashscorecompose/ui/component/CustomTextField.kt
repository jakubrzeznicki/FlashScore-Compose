package com.kuba.flashscorecompose.ui.component


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 06/02/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    value: String,
    onNewValue: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onNewValue(it) },
        label = {
            Text(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Mail,
                contentDescription = stringResource(id = R.string.email)
            )
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
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // onImeAction()
            }
        ),
    )
}

@Composable
fun NormalPasswordField(
    modifier: Modifier = Modifier,
    value: String,
    onNewValue: (String) -> Unit,
) {
    PasswordField(
        labelId = R.string.password,
        modifier = modifier,
        value = value,
        onNewValue = onNewValue
    )
}

@Composable
fun RepeatPasswordField(
    modifier: Modifier = Modifier,
    value: String,
    onNewValue: (String) -> Unit,
) {
    PasswordField(
        labelId = R.string.repeat_password,
        modifier = modifier,
        value = value,
        onNewValue = onNewValue
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(
    @StringRes labelId: Int,
    modifier: Modifier = Modifier,
    value: String,
    onNewValue: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Done
) {
    val showPassword = rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = { onNewValue(it) },
        modifier = modifier.fillMaxWidth(),
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
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        shape = RoundedCornerShape(16.dp),
        label = {
            Text(
                text = stringResource(id = labelId),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Key,
                contentDescription = stringResource(id = R.string.password)
            )
        },
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.hide_password)
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.show_password)
                    )
                }
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                //onImeAction()
            }
        ),
    )
}