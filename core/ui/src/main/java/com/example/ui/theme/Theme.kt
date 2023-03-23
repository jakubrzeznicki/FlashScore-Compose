package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
    surfaceVariant = GreyDark,
    onSurface = White,
    inverseSurface = GreyLight,
    inverseOnSurface = GreyTextDark,
    outline = Grey
)

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
    surfaceVariant = GreyDark,
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

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = BlackBackground,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = BlackBackground,
            darkIcons = false
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = FlashScoreTypography,
        shapes = Shapes,
        content = content
    )
}
