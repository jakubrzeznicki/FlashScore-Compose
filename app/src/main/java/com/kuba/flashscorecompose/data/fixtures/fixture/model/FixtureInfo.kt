package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FixtureInfo(
    val date: String,
    val id: Int,
    val referee: String,
    val status: Status,
    val timestamp: Int,
    val timezone: String,
    val venue: Venue,
    val periods: Periods
) : Parcelable{
    companion object {
        val EMPTY_FIXTURE_INFO =
            FixtureInfo(
                "",
                0,
                "",
                Status.EMPTY_STATUS,
                0,
                "",
                Venue.EMPTY_VENUE,
                Periods.EMPTY_PERIODS
            )
    }
}