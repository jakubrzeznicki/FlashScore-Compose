package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem

/**
 * Created by jrzeznicki on 17/01/2023.
 */

const val VERSUS = "vs"
const val DASH = "-"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixtureCard(fixtureItem: FixtureItem, onFixtureClick: (FixtureItem) -> Unit = {}) {
    Card(
        onClick = { onFixtureClick(fixtureItem) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.inverseSurface
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeamLogos(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 8.dp),
                fixtureItem = fixtureItem
            )
            FixtureDetails(
                modifier = Modifier
                    .weight(7f)
                    .padding(end = 4.dp),
                fixtureItem = fixtureItem
            )
            FixtureStatus(
                modifier = Modifier
                    .weight(2f)
                    .background(
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                        color = MaterialTheme.colorScheme.surface
                    )
                    .size(70.dp),
                fixtureItem = fixtureItem
            )
        }
    }
}

@Composable
fun TeamLogos(modifier: Modifier, fixtureItem: FixtureItem) {
    Row(modifier = modifier) {
        LogoWithBackground(
            logo = fixtureItem.homeTeam.logo,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(30.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                .size(36.dp)
        )
        LogoWithBackground(
            logo = fixtureItem.awayTeam.logo,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(30.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                .size(36.dp)
        )
    }
}

@Composable
fun LogoWithBackground(logo: String, modifier: Modifier) {
    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = PaddingValues(4.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data(logo)
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun FixtureDetails(modifier: Modifier, fixtureItem: FixtureItem) {
    Row(modifier = modifier) {
        FixtureDetailsColumn(
            modifier = Modifier.weight(6f),
            firstLine = fixtureItem.homeTeam.name,
            secondLine = fixtureItem.goals.home.toString()
        )
        FixtureDetailsColumn(
            modifier = Modifier.weight(2f),
            firstLine = VERSUS,
            secondLine = DASH
        )
        FixtureDetailsColumn(
            modifier = Modifier.weight(6f),
            firstLine = fixtureItem.awayTeam.name,
            secondLine = fixtureItem.goals.away.toString()
        )
    }
}

@Composable
fun FixtureDetailsColumn(modifier: Modifier, firstLine: String, secondLine: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = firstLine,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = secondLine,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }
}

@Composable
fun FixtureStatus(modifier: Modifier, fixtureItem: FixtureItem) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = fixtureItem.fixture.status.short,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }
}