package com.kuba.flashscorecompose.data.standings.mapper

import com.kuba.flashscorecompose.data.league.mapper.toLeague
import com.kuba.flashscorecompose.data.league.mapper.toLeagueEntity
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.local.model.GoalsStandingEntity
import com.kuba.flashscorecompose.data.standings.local.model.InformationStandingEntity
import com.kuba.flashscorecompose.data.standings.local.model.StandingItemEntity
import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import com.kuba.flashscorecompose.data.standings.model.GoalsStanding
import com.kuba.flashscorecompose.data.standings.model.InformationStanding
import com.kuba.flashscorecompose.data.standings.model.Standing
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.data.standings.remote.model.GoalsStandingDto
import com.kuba.flashscorecompose.data.standings.remote.model.InformationStandingDto
import com.kuba.flashscorecompose.data.standings.remote.model.StandingItemDto
import com.kuba.flashscorecompose.data.standings.remote.model.StandingsDto
import com.kuba.flashscorecompose.data.team.information.mapper.toTeam
import com.kuba.flashscorecompose.data.team.information.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.team.information.model.Team

/**
 * Created by jrzeznicki on 18/01/2023.
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

fun StandingsEntity.toStandings(): Standing {
    return Standing(
        league = league.toLeague(),
        leagueId = leagueId,
        season = season,
        standingItems = standings.map { it.toStandingItem() }
    )
}

fun StandingItemEntity.toStandingItem(): StandingItem {
    return StandingItem(
        all = all.toInformationStanding(),
        away = away.toInformationStanding(),
        home = home.toInformationStanding(),
        selectedInformationStanding = all.toInformationStanding(),
        description = description,
        form = form,
        goalsDiff = goalsDiff,
        group = group,
        points = points,
        rank = rank,
        status = status,
        team = team.toTeam(),
        update = update
    )
}

fun InformationStandingEntity.toInformationStanding(): InformationStanding {
    return InformationStanding(
        draw = draw,
        goals = goals.toGoalsStanding(),
        lose = lose,
        played = played,
        win = win
    )
}

fun GoalsStandingEntity.toGoalsStanding(): GoalsStanding {
    return GoalsStanding(against = against, forValue = forValue)
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
            round = "",
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