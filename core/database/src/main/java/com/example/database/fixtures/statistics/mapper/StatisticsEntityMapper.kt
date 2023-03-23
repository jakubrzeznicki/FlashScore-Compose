package com.example.database.fixtures.statistics.mapper

import com.example.database.fixtures.statistics.model.StatisticRowEntity
import com.example.database.fixtures.statistics.model.StatisticsEntity
import com.example.database.teams.mapper.toTeam
import com.example.model.statistics.Statistic
import com.example.model.statistics.Statistics

/**
 * Created by jrzeznicki on 13/03/2023.
 */
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
