package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.kuba.flashscorecompose.ui.theme.GreyLight
import com.kuba.flashscorecompose.ui.theme.GreyTextDark

/**
 * Created by jrzeznicki on 19/01/2023.
 */
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
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = GreyTextDark,
            backgroundColor = GreyLight,
            focusedIndicatorColor = Color.Transparent,
            focusedLabelColor = GreyTextDark,
            cursorColor = GreyTextDark,
            unfocusedLabelColor = GreyTextDark,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = GreyTextDark
        ),
        enabled = isEnabled
    )
}