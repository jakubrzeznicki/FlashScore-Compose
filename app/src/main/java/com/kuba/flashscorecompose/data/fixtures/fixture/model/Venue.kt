package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Venue(val city: String, val id: Int, val name: String): Parcelable {
    companion object {
        val EMPTY_VENUE = Venue("", 0, "")
    }
}