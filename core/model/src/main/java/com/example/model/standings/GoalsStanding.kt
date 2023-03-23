package com.example.model.standings

data class GoalsStanding(val against: Int, val forValue: Int) {
    companion object {
        val EMPTY_GOALS_STANDING = GoalsStanding(0, 0)
    }
}
