package com.kuba.flashscorecompose.data.team.information.model

import android.os.Parcelable
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Colors
import kotlinx.parcelize.Parcelize

@Parcelize
data class Team(
    val id: Int,
    val logo: String,
    val name: String,
    val isWinner: Boolean,
    val code: String,
    val country: String,
    val founded: Int,
    val isNational: Boolean,
    val colors: Colors
) : Parcelable {
    companion object {
        val EMPTY_TEAM = Team(0, "", "", false, "", "", 0, false, Colors.EMPTY_COLORS)
    }
}