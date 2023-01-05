package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class Venue(val city: String, val id: Int, val name: String) {
    companion object {
        val EMPTY_VENUE = Venue("", 0, "")
    }
}