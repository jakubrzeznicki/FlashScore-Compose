package com.example.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.model.player.PlayerWrapper
import com.example.ui.R

/**
 * Created by jrzeznicki on 02/02/2023.
 */

@Composable
fun PlayerCard(
    playerWrapper: PlayerWrapper,
    onPlayerClick: (PlayerWrapper) -> Unit,
    onFavoriteClick: (PlayerWrapper) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clickable { onPlayerClick(playerWrapper) }
            ) {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .decoderFactory(SvgDecoder.Factory())
                        .data(playerWrapper.player.photo)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = "${playerWrapper.player.firstname} ${playerWrapper.player.lastname}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(18.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                .decoderFactory(SvgDecoder.Factory())
                                .data(playerWrapper.country.flag)
                                .size(Size.ORIGINAL)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = playerWrapper.country.name,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { onFavoriteClick(playerWrapper) },
                    modifier = Modifier.size(32.dp)
                ) {
                    val (icon, color) = if (playerWrapper.isFavorite) {
                        Icons.Filled.Favorite to MaterialTheme.colorScheme.error
                    } else {
                        Icons.Filled.FavoriteBorder to MaterialTheme.colorScheme.onSecondary
                    }
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = icon,
                        contentDescription = "",
                        tint = color
                    )
                }
            }
        }
    }
}