package com.kuba.flashscorecompose.data.fixtures.statistics.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeam
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.fixtures.statistics.local.model.StatisticRowEntity
import com.kuba.flashscorecompose.data.fixtures.statistics.local.model.StatisticsEntity
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistic
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticDto
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticsDataDto
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticsTeamDto

/**
 * Created by jrzeznicki on 03/01/2023.
 */

fun Statistics.toStatisticsEntity(fixtureId: Int, isHome: Boolean): StatisticsEntity {
    return StatisticsEntity(
        teamId = team.id,
        fixtureId = fixtureId,
        statistics = statistics.map { it.toStatisticRowEntity() },
        team = team.toTeamEntity(null),
        isHome = isHome
    )
}

fun Statistic.toStatisticRowEntity(): StatisticRowEntity {
    return StatisticRowEntity(type = type, value = value)
}

fun StatisticsEntity.toStatistics(): Statistics {
    return Statistics(
        statistics = statistics.map { it.toStatistic() },
        team = team.toTeam(),
        fixtureId = fixtureId
    )
}

fun StatisticRowEntity.toStatistic(): Statistic {
    return Statistic(type = type, value = value)
}

fun StatisticsTeamDto.toStatistics(fixtureId: Int, isHome: Boolean): Statistics {
    return Statistics(
        fixtureId = fixtureId,
        statistics = statistics?.map { it.toStatistic() }.orEmpty(),
        team = team?.toTeam() ?: Team.EMPTY_TEAM,
    )
}

fun StatisticDto.toStatistic(): Statistic {
    return Statistic(
        type = type.orEmpty(),
        value = (if (value != null) {
            when (value) {
                is Float -> value.toInt()
                is Double -> value.toInt()
                is Int -> value.toInt()
                is String -> value.toString()
                else -> value
            }
        } else {
            0
        }).toString()
    )
}