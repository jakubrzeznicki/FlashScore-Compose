package com.example.ui.composables

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
import com.example.model.country.Country
import com.example.ui.R

/**
 * Created by jrzeznicki on 19/01/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryWidgetCard(
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
            .size(width = 130.dp, height = 120.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
                placeholder = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(4.dp))
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
