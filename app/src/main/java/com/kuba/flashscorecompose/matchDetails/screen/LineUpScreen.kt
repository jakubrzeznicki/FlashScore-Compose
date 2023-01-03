package com.kuba.flashscorecompose.matchDetails.screen

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.relay.compose.BoxScopeInstanceImpl.matchParentSize
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.ui.theme.*

/**
 * Created by jrzeznicki on 23/12/2022.
 */
@Composable
fun LineUpScreen(lineUp: LineUp) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .matchParentSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colors.background)
            .padding(PaddingValues(vertical = 16.dp)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormationInfoRow(lineUp = lineUp)
        Spacer(modifier = Modifier.size(16.dp))
        TeamFormationButtons()
        Spacer(modifier = Modifier.size(16.dp))
        FormationImage(lineUp)
    }
}

@Composable
fun FormationInfoRow(lineUp: LineUp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(vertical = 8.dp, horizontal = 32.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Formation",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "(${lineUp.formation})",
            fontSize = 14.sp,
            color = TextGreyLight,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TeamFormationButtons() {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextButtonTab(text = "Arsenal", isActive = true)
        TextButtonTab(text = "Chelsea", isActive = false)
    }
}

@Composable
fun TextButtonTab(text: String, isActive: Boolean) {
    TextButton(
        onClick = { },
        modifier = if (isActive) {
            Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(colors = listOf(LightOrange, Orange))
                )
                .padding(horizontal = 16.dp)
        } else {
            Modifier
                .clip(RoundedCornerShape(50))
        },
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

@Composable
fun FormationImage(lineUp: LineUp) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Image(
            painter = painterResource(id = R.drawable.football_pitch),
            contentDescription = "",
            modifier = Modifier
                .drawWithContent {
                    clipRect(bottom = size.height / 1.6f) {
                        this@drawWithContent.drawContent()
                    }
                }
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )
        PlayersGrid(players = lineUp.players, formation = lineUp.formation)
    }
}

@Composable
fun PlayerLineUpItem(player: Player) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .background(shape = RoundedCornerShape(24.dp), color = GreenLight)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.3f)
                )
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.number.toString(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(color = GreenDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.name,
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.White,
            )
        }
    }
}

@Composable
fun PlayersGrid(players: List<Player>, formation: String) {
    val gridMap = players.groupBy { player ->
        player.grid.first().toString().toInt()
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        gridMap.keys.forEach {
            val formationLine = gridMap.getValue(it)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                formationLine.forEach { player ->
                    PlayerLineUpItem(player = player)
                }
            }
        }
    }
}