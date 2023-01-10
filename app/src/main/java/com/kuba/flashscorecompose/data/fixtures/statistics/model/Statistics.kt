package com.kuba.flashscorecompose.data.fixtures.statistics.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team

data class Statistics(
    val uuid: String,
    val fixtureId: Int,
    val statistics: List<Statistic>,
    val team: Team
)