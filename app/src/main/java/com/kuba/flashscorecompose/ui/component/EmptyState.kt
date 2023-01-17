package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kuba.flashscorecompose.ui.theme.Blue500
import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 17/01/2023.
 */
@Composable
fun EmptyState(
    modifier: Modifier,
    iconId: Int,
    contentDescriptionId: Int,
    textId: Int,
    onRefreshClick: () -> Unit
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
            contentDescription = stringResource(id = contentDescriptionId)
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = textId)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = onRefreshClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = Blue500)
        ) {
            Text(
                stringResource(id = R.string.refresh),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}