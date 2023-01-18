package com.kuba.flashscorecompose.data.fixtures.fixture.model

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
    val image: String
) : Parcelable {
    companion object {
        val EMPTY_VENUE = Venue("", 0, "", "", 0, "", "")
    }
}