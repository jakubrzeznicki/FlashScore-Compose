package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class FixtureInfo(
    val date: String,
    val id: Int,
    val referee: String,
    val status: Status,
    val timestamp: Int,
    val timezone: String,
    val venue: Venue
) {
    companion object {
        val EMPTY_FIXTURE_INFO =
            FixtureInfo("", 0, "", Status.EMPTY_STATUS, 0, "", Venue.EMPTY_VENUE)
    }
}