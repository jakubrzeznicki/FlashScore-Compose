package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class Goals(val home: Int, val away: Int) {
    companion object {
        val EMPTY_GOALS = Goals(0, 0)
    }
}