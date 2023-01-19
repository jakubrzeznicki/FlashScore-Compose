package com.kuba.flashscorecompose.data.standings.model

data class InformationStanding(
    val draw: Int,
    val goals: GoalsStanding,
    val lose: Int,
    val played: Int,
    val win: Int
) {
    companion object {
        val EMPTY_INFORMATION_STANDING =
            InformationStanding(0, GoalsStanding.EMPTY_GOALS_STANDING, 0, 0, 0)
    }
}