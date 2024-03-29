package com.example.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.ui.R
import androidx.compose.material3.Text as Text1

/**
 * Created by jrzeznicki on 01/02/2023.
 */

@Composable
fun HeaderDetailsWithImage(name: String, image: String, onHeaderClick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onHeaderClick?.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(shape = CircleShape, color = MaterialTheme.colorScheme.surface)
                .border(
                    width = 2.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                .size(112.dp)
        ) {
            if (image.isEmpty()) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .size(90.dp),
                    alignment = Alignment.Center,
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight
                )
            } else {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .size(90.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .decoderFactory(SvgDecoder.Factory())
                        .data(image)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_profile),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text1(
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
    }
}
