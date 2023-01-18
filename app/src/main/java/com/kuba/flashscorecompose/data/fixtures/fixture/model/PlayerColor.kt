package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerColor(val border: String, val number: String, val primary: String) : Parcelable {

    companion object {
        val EMPTY_PLAYER_COLOR = PlayerColor("", "", "")
    }
}