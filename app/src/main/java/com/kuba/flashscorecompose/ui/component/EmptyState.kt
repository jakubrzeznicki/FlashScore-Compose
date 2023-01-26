package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Created by jrzeznicki on 17/01/2023.
 */
@Composable
fun EmptyState(
    modifier: Modifier,
    iconId: Int,
    contentDescriptionId: Int,
    textId: Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(128.dp)
                .padding(8.dp),
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = contentDescriptionId),
            tint = MaterialTheme.colorScheme.inverseOnSurface
        )
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = stringResource(id = textId),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            textAlign = TextAlign.Center
        )
    }
}