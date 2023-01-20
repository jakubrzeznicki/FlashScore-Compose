package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.ui.theme.LightOrange
import com.kuba.flashscorecompose.ui.theme.Orange

/**
 * Created by jrzeznicki on 20/01/2023.
 */
@Composable
fun FilterTextButtonTab(text: String, isActive: Boolean, onStateChange: () -> Unit) {
    TextButton(
        onClick = onStateChange,
        modifier = if (isActive) {
            Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(colors = listOf(LightOrange, Orange))
                )
                .padding(horizontal = 4.dp)
        } else {
            Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(50))
        }
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}