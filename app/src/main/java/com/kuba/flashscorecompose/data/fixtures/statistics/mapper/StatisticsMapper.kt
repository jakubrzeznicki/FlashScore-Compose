package com.kuba.flashscorecompose.data.fixtures.statistics.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeam
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.fixtures.statistics.local.model.StatisticRowEntity
import com.kuba.flashscorecompose.data.fixtures.statistics.local.model.StatisticsEntity
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistic
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticDto
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticsTeamDto

/**
 * Created by jrzeznicki on 03/01/2023.
 */

fun Statistics.toStatisticsEntity(): StatisticsEntity {
    return StatisticsEntity(
        id = id,
        fixtureId = fixtureId,
        statistics = statistics.map { it.toStatisticRowEntity() },
        team = team.toTeamEntity()
    )
}

fun Statistic.toStatisticRowEntity(): StatisticRowEntity {
    return StatisticRowEntity(type = type.orEmpty(), value = value ?: 0)
}

fun StatisticsEntity.toStatistics(): Statistics {
    return Statistics(
        id = id,
        fixtureId = fixtureId,
        statistics = statistics.map { it.toStatistic() },
        team = team.toTeam()
    )
}

fun StatisticRowEntity.toStatistic(): Statistic {
    return Statistic(type = type.orEmpty(), value = value ?: 0)
}

fun StatisticsTeamDto.toStatistics(fixtureId: Int): Statistics {
    return Statistics(
        id = 0, //random,
        fixtureId = fixtureId,
        statistics = statistics?.map { it.toStatistic() }.orEmpty(),
        team = team?.toTeam() ?: Team.EMPTY_TEAM
    )
}

fun StatisticDto.toStatistic(): Statistic {
    return Statistic(type = type.orEmpty(), value = value ?: 0)
}