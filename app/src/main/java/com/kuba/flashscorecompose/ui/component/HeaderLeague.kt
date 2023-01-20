package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.ui.theme.Grey

/**
 * Created by jrzeznicki on 19/01/2023.
 */

@Composable
fun HeaderLeague(league: League, onLeagueClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { onLeagueClick(league.id) }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(24.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(league.countryFlag)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    text = league.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = league.countryName,
                    fontSize = 12.sp,
                    color = Grey
                )
            }
        }
        IconButton(modifier = Modifier
            .padding(horizontal = 12.dp)
            .size(16.dp), onClick = { }) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}