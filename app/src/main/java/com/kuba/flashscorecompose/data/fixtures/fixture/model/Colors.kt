package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Colors(val goalkeeper: PlayerColor, val player: PlayerColor) : Parcelable {

    companion object {
        val EMPTY_COLORS =
            Colors(PlayerColor.EMPTY_PLAYER_COLOR, PlayerColor.EMPTY_PLAYER_COLOR)
    }
}