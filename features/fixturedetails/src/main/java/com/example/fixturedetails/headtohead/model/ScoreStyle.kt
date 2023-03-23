package com.example.fixturedetails.headtohead.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.Blue
import com.example.ui.theme.GreenLight
import com.example.ui.theme.RedDark

/**
 * Created by jrzeznicki on 03/02/2023.
 */
sealed class ScoreStyle(val fontWeight: FontWeight, val color: Color, val text: String) {
    object Win : ScoreStyle(FontWeight.Bold, GreenLight, "W")
    object Lose : ScoreStyle(FontWeight.Normal, RedDark, "L")
    object Draw : ScoreStyle(FontWeight.SemiBold, Blue, "D")
}
