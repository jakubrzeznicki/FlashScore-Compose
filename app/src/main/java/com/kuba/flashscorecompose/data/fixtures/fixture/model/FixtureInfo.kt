package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import com.kuba.flashscorecompose.data.team.information.model.Venue
import kotlinx.parcelize.Parcelize

@Parcelize
data class FixtureInfo(
    val date: String,
    val id: Int,
    val referee: String,
    val status: Status,
    val timestamp: Long,
    val timezone: String,
    val venue: Venue,
    val periods: Periods,
    val year: String,
    val isLive: Boolean
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
                Periods.EMPTY_PERIODS,
                "",
                false
            )
    }
}