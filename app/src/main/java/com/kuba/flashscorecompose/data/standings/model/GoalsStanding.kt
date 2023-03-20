package com.kuba.flashscorecompose.data.standings.model

data class GoalsStanding(val against: Int, val forValue: Int) {
    companion object {
        val EMPTY_GOALS_STANDING = GoalsStanding(0, 0)
    }
}
