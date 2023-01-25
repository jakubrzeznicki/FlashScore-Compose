package com.kuba.flashscorecompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = LightOrange,
    onPrimary = White,
    secondary = Orange,
    onSecondary = White,
    tertiary = Blue,
    onTertiary = White,
    tertiaryContainer = BlueDark,
    onTertiaryContainer = White,
    error = RedDark,
    background = BlackBackground,
    onBackground = GreyTextLight,
    surface = GreyDark,
    onSurface = White,
    inverseSurface = GreyLight,
    inverseOnSurface = GreyTextDark,
    outline = Grey
)
//import androidx.compose.ui.graphics.Color

//import com.kuba.flashscorecompose.ui.theme.GreyTextLight

private val LightColorPalette = lightColorScheme(
    primary = LightOrange,
    onPrimary = White,
    secondary = Orange,
    onSecondary = White,
    tertiary = Blue,
    onTertiary = White,
    tertiaryContainer = BlueDark,
    onTertiaryContainer = White,
    error = RedDark,
    background = BlackBackground,
    onBackground = GreyTextLight,
    surface = GreyDark,
    onSurface = White,
    inverseSurface = GreyLight,
    inverseOnSurface = GreyTextDark,
    outline = Grey
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
        colorScheme = colors,
        typography = FlashScoreTypography,
        shapes = Shapes,
        content = content
    )
}