package com.kuba.flashscorecompose.data.fixtures.statistics.model

import com.kuba.flashscorecompose.data.team.information.model.Team

data class Statistics(
    val fixtureId: Int,
    val statistics: List<Statistic>,
    val team: Team
) {
    companion object {
        val EMPTY_STATISTICS = Statistics(0, emptyList(), Team.EMPTY_TEAM)
    }
}
