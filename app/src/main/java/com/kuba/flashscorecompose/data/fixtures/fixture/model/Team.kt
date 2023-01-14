package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Team(val id: Int, val logo: String, val name: String, val winner: Boolean) : Parcelable {
    companion object {
        val EMPTY_TEAM = Team(0, "", "", false)
    }
}