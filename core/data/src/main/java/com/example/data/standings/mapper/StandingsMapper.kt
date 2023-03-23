package com.example.data.standings.mapper

import com.example.data.league.mapper.toLeagueEntity
import com.example.data.team.mapper.toTeam
import com.example.data.team.mapper.toTeamEntity
import com.example.database.standings.model.GoalsStandingEntity
import com.example.database.standings.model.InformationStandingEntity
import com.example.database.standings.model.StandingItemEntity
import com.example.database.standings.model.StandingsEntity
import com.example.model.league.League
import com.example.model.standings.GoalsStanding
import com.example.model.standings.InformationStanding
import com.example.model.standings.Standing
import com.example.model.standings.StandingItem
import com.example.model.team.Team
import com.example.network.model.standing.GoalsStandingDto
import com.example.network.model.standing.InformationStandingDto
import com.example.network.model.standing.StandingItemDto
import com.example.network.model.standing.StandingsDto

/**
 * Created by jrzeznicki on 14/03/2023.
 */
fun Standing.toStandingsEntity(): StandingsEntity {
    return StandingsEntity(
        league = league.toLeagueEntity(),
        leagueId = leagueId,
        season = season,
        standings = standingItems.map {
            it.toStandingItemEntity(
                league.id,
                season,
                league.countryName
            )
        }
    )
}

fun StandingItem.toStandingItemEntity(
    leagueId: Int,
    season: Int,
    countryName: String
): StandingItemEntity {
    return StandingItemEntity(
        all = all.toInformationStandingEntity(),
        away = away.toInformationStandingEntity(),
        home = home.toInformationStandingEntity(),
        description = description,
        form = form,
        goalsDiff = goalsDiff,
        group = group,
        points = points,
        rank = rank,
        status = status,
        team = team.toTeamEntity(leagueId, season, countryName),
        update = update
    )
}

fun InformationStanding.toInformationStandingEntity(): InformationStandingEntity {
    return InformationStandingEntity(
        draw = draw,
        goals = goals.toGoalsStandingEntity(),
        lose = lose,
        played = played,
        win = win
    )
}

fun GoalsStanding.toGoalsStandingEntity(): GoalsStandingEntity {
    return GoalsStandingEntity(against = against, forValue = forValue)
}

fun StandingsDto.toStandings(): Standing {
    return Standing(
        league = League(
            id = id ?: 0,
            name = name.orEmpty(),
            logo = logo.orEmpty(),
            countryName = country.orEmpty(),
            countryFlag = flag.orEmpty(),
            season = season ?: 0,
            type = "",
            countryCode = "",
            round = ""
        ),
        leagueId = id ?: 0,
        season = season ?: 0,
        standingItems = standings.flatMap {
            it?.map { standingItemDto ->
                standingItemDto.toStandingItem(id ?: 0, season ?: 0, country.orEmpty())
            }.orEmpty()
        }
    )
}

fun StandingItemDto.toStandingItem(leagueId: Int, season: Int, countryName: String): StandingItem {
    return StandingItem(
        all = all?.toInformationStanding() ?: InformationStanding.EMPTY_INFORMATION_STANDING,
        away = away?.toInformationStanding() ?: InformationStanding.EMPTY_INFORMATION_STANDING,
        home = home?.toInformationStanding() ?: InformationStanding.EMPTY_INFORMATION_STANDING,
        selectedInformationStanding = all?.toInformationStanding()
            ?: InformationStanding.EMPTY_INFORMATION_STANDING,
        description = description.orEmpty(),
        form = form.orEmpty(),
        goalsDiff = goalsDiff ?: 0,
        group = group.orEmpty(),
        points = points ?: 0,
        rank = rank ?: 0,
        status = status.orEmpty(),
        team = team?.toTeam(leagueId, season, countryName) ?: Team.EMPTY_TEAM,
        update = update.orEmpty()
    )
}

fun InformationStandingDto.toInformationStanding(): InformationStanding {
    return InformationStanding(
        draw = draw ?: 0,
        goals = goals?.toGoalsStanding() ?: GoalsStanding.EMPTY_GOALS_STANDING,
        lose = lose ?: 0,
        played = played ?: 0,
        win = win ?: 0
    )
}

fun GoalsStandingDto.toGoalsStanding(): GoalsStanding {
    return GoalsStanding(against = against ?: 0, forValue = forValue ?: 0)
}
