package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun FixtureCard(
    fixtureItem: FixtureItem,
    onFixtureClick: (FixtureItem) -> Unit,
    onFavoriteClick: (FixtureItem) -> Unit = {}
) {
    Card(
        onClick = { onFixtureClick(fixtureItem) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TeamLogos(fixtureItem = fixtureItem)
                FixtureDateInfo(formattedDate = fixtureItem.fixture.formattedDate)
            }
            FixtureDetails(
                modifier = Modifier
                    .weight(7f)
                    .padding(end = 4.dp, top = 4.dp, bottom = 4.dp),
                fixtureItem = fixtureItem
            )
            FixtureStatus(
                modifier = Modifier
                    .weight(2f)
                    .background(
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                        color = MaterialTheme.colorScheme.surface
                    )
                    .size(72.dp)
                    .padding(vertical = 4.dp),
                fixtureItem = fixtureItem,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun TeamLogos(fixtureItem: FixtureItem) {
    Row(modifier = Modifier.padding(bottom = 4.dp)) {
        LogoWithBackground(
            logo = fixtureItem.homeTeam.logo,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(30.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                .size(32.dp)
        )
        LogoWithBackground(
            logo = fixtureItem.awayTeam.logo,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(30.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                .size(32.dp)
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
            placeholder = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun FixtureDateInfo(formattedDate: String) {
    Text(
        text = formattedDate,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.inverseOnSurface,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        textAlign = TextAlign.Center
    )
}

@Composable
fun FixtureDetails(modifier: Modifier, fixtureItem: FixtureItem) {
    val isNotStarted = !fixtureItem.fixture.isStarted
    val isLive = fixtureItem.fixture.isLive
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FixtureDetailsColumn(
                modifier = Modifier.weight(6f),
                fixtureItem.homeTeam.name,
                secondLine = fixtureItem.goals.home.toString(),
                isNotStarted
            )
            FixtureDetailsColumn(
                modifier = Modifier.weight(2f),
                firstLine = VERSUS,
                secondLine = DASH,
                isNotStarted
            )
            FixtureDetailsColumn(
                modifier = Modifier.weight(6f),
                fixtureItem.awayTeam.name,
                secondLine = fixtureItem.goals.away.toString(),
                isNotStarted
            )
        }
        if (isNotStarted) {
            Text(
                text = stringResource(id = R.string.match_not_started),
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        if (isLive) {
            val liveMinuteLabel = stringResource(id = R.string.live_minute)
            Text(
                text = "$liveMinuteLabel: ${fixtureItem.fixture.status.elapsed}'",
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.error,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun FixtureDetailsColumn(
    modifier: Modifier,
    firstLine: String,
    secondLine: String,
    isNotStarted: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = firstLine,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        if (!isNotStarted) {
            Text(
                text = secondLine,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun FixtureStatus(
    modifier: Modifier,
    fixtureItem: FixtureItem,
    onFavoriteClick: (FixtureItem) -> Unit
) {
    val isLive = fixtureItem.fixture.isLive
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = fixtureItem.fixture.status.short,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = if (isLive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSecondary,
        )
        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = { onFavoriteClick(fixtureItem) }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}