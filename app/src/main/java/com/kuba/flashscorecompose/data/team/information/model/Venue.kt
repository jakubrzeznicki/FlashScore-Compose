package com.kuba.flashscorecompose.data.team.information.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Venue(
    val city: String,
    val id: Int,
    val name: String,
    val address: String,
    val capacity: Int,
    val surface: String,
    val image: String,
    val teamId: Int
) : Parcelable {
    companion object {
        val EMPTY_VENUE = Venue("", 0, "", "", 0, "", "", 0)
    }
}
