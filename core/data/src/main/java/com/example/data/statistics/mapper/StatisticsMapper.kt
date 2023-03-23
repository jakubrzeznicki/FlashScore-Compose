package com.example.data.statistics.mapper

import com.example.data.team.mapper.toTeam
import com.example.data.team.mapper.toTeamEntity
import com.example.database.fixtures.statistics.model.StatisticRowEntity
import com.example.database.fixtures.statistics.model.StatisticsEntity
import com.example.model.statistics.Statistic
import com.example.model.statistics.Statistics
import com.example.model.team.Team
import com.example.network.model.statistics.StatisticDto
import com.example.network.model.statistics.StatisticsTeamDto

/**
 * Created by jrzeznicki on 14/03/2023.
 */
fun Statistics.toStatisticsEntity(fixtureId: Int, isHome: Boolean): StatisticsEntity {
    return StatisticsEntity(
        teamId = team.id,
        fixtureId = fixtureId,
        statistics = statistics.map { it.toStatisticRowEntity() },
        team = team.toTeamEntity(),
        isHome = isHome
    )
}

fun Statistic.toStatisticRowEntity(): StatisticRowEntity {
    return StatisticRowEntity(type = type, value = value)
}

fun StatisticsTeamDto.toStatistics(fixtureId: Int): Statistics {
    return Statistics(
        fixtureId = fixtureId,
        statistics = statistics?.map { it.toStatistic() }.orEmpty(),
        team = team?.toTeam() ?: Team.EMPTY_TEAM
    )
}

fun StatisticDto.toStatistic(): Statistic {
    return Statistic(
        type = type.orEmpty(),
        value = value.getValue()
    )
}

private fun Any?.getValue(): String {
    if (this == null) return "0"
    val value = when (this) {
        is Float -> this.toInt()
        is Double -> this.toInt()
        is Int -> this.toInt()
        is String -> this.toString()
        else -> this
    }
    return value.toString()
}
