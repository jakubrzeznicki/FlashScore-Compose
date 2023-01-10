package com.kuba.flashscorecompose.matchdetails.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.relay.compose.BoxScopeInstanceImpl.matchParentSize
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.home.screen.FixtureData
import com.kuba.flashscorecompose.ui.theme.GreenLight
import com.kuba.flashscorecompose.ui.theme.GreyDark

/**
 * Created by jrzeznicki on 23/12/2022.
 */
@Composable
fun HeadToHeadScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .matchParentSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colors.background)
            .padding(vertical = 16.dp)
    ) {
        LastMatchesSection(
            title = "Ostatnie mecze: Arsenal",
            type = H2HType.LastFirstTeam,
            provideFixtureDataList()
        )
        Spacer(modifier = Modifier.size(16.dp))
        LastMatchesSection(
            title = "Ostatnie mecze: Newcastle",
            type = H2HType.LastSecondTeam,
            provideFixtureDataList()
        )
        Spacer(modifier = Modifier.size(16.dp))
        LastMatchesSection(
            title = "Pojedynki bezpośrednie",
            type = H2HType.HeadToHead,
            provideFixtureDataList()
        )
    }
}

@Composable
fun LastMatchesSection(title: String, type: H2HType, fixtureDataList: List<FixtureData>) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = title,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column() {
            fixtureDataList.forEach {
                HeadToHeadMatchItem(fixtureData = it)
                Divider(
                    color = GreyDark,
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HeadToHeadMatchItem(fixtureData: FixtureData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "2022",
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier.size(24.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .decoderFactory(SvgDecoder.Factory())
                            .data(fixtureData.homeTeam.logo)
                            .size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = fixtureData.homeTeam.name,
                        style = TextStyle(fontSize = 12.sp),
                        color = Color.White
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier.size(24.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .decoderFactory(SvgDecoder.Factory())
                            .data(fixtureData.awayTeam.logo)
                            .size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = fixtureData.awayTeam.name,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                        color = Color.White
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = fixtureData.homeTeamScore.toString(),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = fixtureData.awayTeamScore.toString(),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Box(
                modifier = Modifier
                    .background(color = GreenLight, shape = RoundedCornerShape(8.dp))
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .padding(4.dp)
            ) {
                Text(
                    text = "Z",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 12.sp, fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

sealed class H2HType {
    object LastFirstTeam : H2HType()
    object LastSecondTeam : H2HType()
    object HeadToHead : H2HType()
}