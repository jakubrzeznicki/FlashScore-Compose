package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/**
 * Created by jrzeznicki on 19/01/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    modifier: Modifier = Modifier,
    label: String,
    query: String,
    onQueryChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    isEnabled: Boolean = true
) {
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = modifier,
        value = query,
        onValueChange = { onQueryChange(it) },
        leadingIcon = leadingIcon,
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.titleMedium,
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground,
            containerColor =  MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = MaterialTheme.colorScheme.onBackground
        ),
        enabled = isEnabled
    )
}