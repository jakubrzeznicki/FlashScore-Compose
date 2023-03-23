package com.example.model.fixture

import android.os.Parcelable
import com.example.model.team.Venue
import kotlinx.parcelize.Parcelize

@Parcelize
data class FixtureInfo(
    val date: String,
    val formattedDate: String,
    val shortDate: String,
    val year: Int,
    val id: Int,
    val referee: String,
    val status: Status,
    val timestamp: Long,
    val timezone: String,
    val venue: Venue,
    val periods: Periods,
    val isLive: Boolean,
    val isStarted: Boolean
) : Parcelable {
    companion object {
        const val FORMATTED_DATE_PATTERN = "dd.MM.yyyy HH:mm"
        const val SHORT_DATE_PATTERN = "yyyy-MM-dd"
        val EMPTY_FIXTURE_INFO =
            FixtureInfo(
                "",
                "",
                "",
                0,
                0,
                "",
                Status.EMPTY_STATUS,
                0L,
                "",
                Venue.EMPTY_VENUE,
                Periods.EMPTY_PERIODS,
                false,
                false
            )
    }
}
