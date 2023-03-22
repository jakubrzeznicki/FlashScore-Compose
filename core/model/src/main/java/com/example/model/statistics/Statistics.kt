package com.example.model.statistics

import com.example.model.team.Team

data class Statistics(
    val fixtureId: Int,
    val statistics: List<Statistic>,
    val team: Team
) {
    companion object {
        val EMPTY_STATISTICS = Statistics(0, emptyList(), Team.EMPTY_TEAM)
    }
}
