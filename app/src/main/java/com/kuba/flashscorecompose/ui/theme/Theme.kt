package com.kuba.flashscorecompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Black500,
    primaryVariant = Black500,
    onPrimary = Color.White,
    secondary = Grey500,
    onSecondary = Color.White,
    error = Red800,
    background = Black500,
    surface = MarineBlue500,
)

private val LightColorPalette = lightColors(
    primary = Black500,
    primaryVariant = Black500,
    onPrimary = Color.White,
    secondary = Grey500,
    onSecondary = Color.White,
    error = Red800,
    background = Black500,
    surface = MarineBlue500,
)

@Composable
fun FlashScoreComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = FlashScoreTypography,
        shapes = Shapes,
        content = content
    )
}