package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class Team(val id: Int, val logo: String, val name: String, val winner: Boolean) {
    companion object {
        val EMPTY_TEAM = Team(0, "", "", false)
    }
}