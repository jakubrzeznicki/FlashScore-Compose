package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class Periods(val first: Int, val second: Int) {
    companion object {
        val EMPTY_PERIODS = Periods(0, 0)
    }
}