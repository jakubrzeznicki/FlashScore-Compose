package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.ui.theme.GreyLight
import com.kuba.flashscorecompose.ui.theme.LightOrange
import com.kuba.flashscorecompose.ui.theme.Orange

/**
 * Created by jrzeznicki on 19/01/2023.
 */

@Composable
fun CountriesWidget(
    modifier: Modifier = Modifier,
    countries: List<Country>,
    onCountryClick: (String, Boolean) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    val selectedCountryName = remember { mutableStateOf("") }
    LazyRow(modifier = modifier, state = state) {
        items(countries) { country ->
            CountryWidgetCard(
                country = country,
                isSelected = selectedCountryName.value == country.name
            ) {
                val isSelected = selectedCountryName.value == it
                selectedCountryName.value = if (isSelected) "" else it
                onCountryClick(it, isSelected)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CountryWidgetCard(
    country: Country,
    isSelected: Boolean,
    onCountryClick: (String) -> Unit
) {
    Card(
        onClick = { onCountryClick(country.name) },
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.size(width = 115.dp, height = 130.dp),
    ) {
        val isSelectedModifier = Modifier
            .padding(PaddingValues(7.dp))
            .background(
                shape = RoundedCornerShape(16.dp),
                brush = Brush.horizontalGradient(colors = listOf(LightOrange, Orange))
            )
        val normalModifier = Modifier
            .padding(PaddingValues(7.dp))
            .background(
                shape = RoundedCornerShape(16.dp), color = GreyLight
            )
        Column(
            modifier = if (isSelected) isSelectedModifier else normalModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                modifier = Modifier.size(40.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(country.flag)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = country.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}