package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.country.model.Country

/**
 * Created by jrzeznicki on 19/01/2023.
 */

@Composable
fun CountriesWidget(
    modifier: Modifier = Modifier,
    countries: List<Country>,
    onCountryClick: (Country, Boolean) -> Unit,
    selectedItem: Country,
    state: LazyListState = rememberLazyListState()
) {
    LazyRow(modifier = modifier, state = state) {
        items(countries) { country ->
            CountryWidgetCard(
                country = country,
                isSelected = selectedItem == country,
                onCountryClick = onCountryClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryWidgetCard(
    country: Country,
    isSelected: Boolean,
    onCountryClick: (Country, Boolean) -> Unit
) {
    val color =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    Card(
        onClick = { onCountryClick(country, isSelected) },
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .padding(8.dp)
            .align(Alignment.Center),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        //.size(width = 115.dp, height = 130.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = color)
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AsyncImage(
                modifier = Modifier.size(40.dp),
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(country.flag)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = country.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}